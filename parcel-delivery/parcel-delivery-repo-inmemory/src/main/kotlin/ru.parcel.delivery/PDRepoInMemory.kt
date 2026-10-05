package ru.parcel.delivery.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.parcel.delivery.repo.common.IRepoPDInitializable
import ru.parceldelivery.common.models.PDLock
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId
import ru.parceldelivery.common.repo.DbPDFilterRequest
import ru.parceldelivery.common.repo.DbPDIdRequest
import ru.parceldelivery.common.repo.DbPDRequest
import ru.parceldelivery.common.repo.DbPDResponseOk
import ru.parceldelivery.common.repo.DbPDsResponseOk
import ru.parceldelivery.common.repo.IDbPDResponse
import ru.parceldelivery.common.repo.IDbPDsResponse
import ru.parceldelivery.common.repo.IRepoPD
import ru.parceldelivery.common.repo.PDRepoBase
import ru.parceldelivery.common.repo.errorDb
import ru.parceldelivery.common.repo.errorEmptyId
import ru.parceldelivery.common.repo.errorEmptyLock
import ru.parceldelivery.common.repo.errorNotFound
import ru.parceldelivery.common.repo.errorRepoConcurrency
import ru.parceldelivery.common.repo.exceptions.RepoEmptyLockException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class PDRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : PDRepoBase(), IRepoPD, IRepoPDInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, PDEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(pds: Collection<PDParcel>) = pds.map { pd ->
        val entity = PDEntity(pd)
        require(entity.trackNumber != null)
        cache.put(entity.trackNumber, entity)
        pd
    }

    override suspend fun createPD(rq: DbPDRequest): IDbPDResponse = tryPDMethod {
        val key = randomUuid()
        val pd = rq.pd.copy(trackNumber = PDParcelId(key), lock = PDLock(randomUuid()))
        val entity = PDEntity(pd)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbPDResponseOk(pd)
    }

    override suspend fun readPD(rq: DbPDIdRequest): IDbPDResponse = tryPDMethod {
        val key = rq.trackNumber.takeIf { it != PDParcelId.NONE }?.asString() ?: return@tryPDMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbPDResponseOk(it.toInternal())
                } ?: errorNotFound(rq.trackNumber)
        }
    }

    override suspend fun updatePD(rq: DbPDRequest): IDbPDResponse = tryPDMethod {
        val rqPD = rq.pd
        val trackNumber = rqPD.trackNumber.takeIf { it != PDParcelId.NONE } ?: return@tryPDMethod errorEmptyId
        val key = trackNumber.asString()
        val oldLock = rqPD.lock.takeIf { it != PDLock.NONE } ?: return@tryPDMethod errorEmptyLock(trackNumber)

        mutex.withLock {
            val oldPD = cache.get(key)?.toInternal()
            when {
                oldPD == null -> errorNotFound(trackNumber)
                oldPD.lock == PDLock.NONE -> errorDb(RepoEmptyLockException(trackNumber))
                oldPD.lock != oldLock -> errorRepoConcurrency(oldPD, oldLock)
                else -> {
                    val newPD = rqPD.copy(lock = PDLock(randomUuid()))
                    val entity = PDEntity(newPD)
                    cache.put(key, entity)
                    DbPDResponseOk(newPD)
                }
            }
        }
    }


    override suspend fun deletePD(rq: DbPDIdRequest): IDbPDResponse = tryPDMethod {
        val trackNumber = rq.trackNumber.takeIf { it != PDParcelId.NONE } ?: return@tryPDMethod errorEmptyId
        val key = trackNumber.asString()
        val oldLock = rq.lock.takeIf { it != PDLock.NONE } ?: return@tryPDMethod errorEmptyLock(trackNumber)

        mutex.withLock {
            val oldPD = cache.get(key)?.toInternal()
            when {
                oldPD == null -> errorNotFound(trackNumber)
                oldPD.lock == PDLock.NONE -> errorDb(RepoEmptyLockException(trackNumber))
                oldPD.lock != oldLock -> errorRepoConcurrency(oldPD, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbPDResponseOk(oldPD)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchPD(rq: DbPDFilterRequest): IDbPDsResponse = tryPDsMethod {
        val result: List<PDParcel> = cache.asMap().asSequence()
            .filter { entry ->
                rq.senderId.takeIf { it != PDUserId.NONE }?.let {
                    it.asString() == entry.value.senderId
                } ?: true
            }
            .filter { entry ->
                rq.receiverId.takeIf { it != PDUserId.NONE }?.let {
                    it.asString() == entry.value.receiverId
                } ?: true
            }
            .filter { entry ->
                rq.status.takeIf { it != PDStatus.NONE }?.let {
                    it.toString() == entry.value.status
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbPDsResponseOk(result)
    }
}
