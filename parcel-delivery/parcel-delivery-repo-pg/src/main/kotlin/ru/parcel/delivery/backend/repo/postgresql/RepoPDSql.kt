package ru.parcel.delivery.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.parceldelivery.common.helpers.asPDError
import ru.parceldelivery.common.models.PDLock
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId
import ru.parcel.delivery.repo.common.IRepoPDInitializable
import ru.parceldelivery.common.repo.*

class RepoPDSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : IRepoPD, IRepoPDInitializable {
    private val pdTable = PDTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        pdTable.deleteAll()
    }

    private fun saveObj(pd: PDParcel): PDParcel = transaction(conn) {
        val trackNumber = pd.trackNumber.takeIf { it.asString().isNotBlank() } ?: PDParcelId(randomUuid())
        val pdToSave = pd.copy(trackNumber = trackNumber)
        pdTable.insert {
            it.to(pdToSave, randomUuid)
        }
        val res = pdTable.selectAll().andWhere { pdTable.trackNumber eq trackNumber.asString() }
            .singleOrNull()
            ?: throw RuntimeException("BD error: saved parcel with track number $trackNumber not found in database")
        pdTable.from(res)
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbPDResponse): IDbPDResponse =
        transactionWrapper(block) { DbPDResponseErr(it.asPDError(code = "db-error")) }

    override fun save(pds: Collection<PDParcel>): Collection<PDParcel> = pds.map { saveObj(it) }

    override suspend fun createPD(rq: DbPDRequest): IDbPDResponse = transactionWrapper {
        DbPDResponseOk(saveObj(rq.pd.copy(lock = PDLock(randomUuid()))))
    }

    private fun repd(trackNumber: PDParcelId): IDbPDResponse {
        val res = pdTable.selectAll().andWhere {
            pdTable.trackNumber eq trackNumber.asString()
        }.singleOrNull() ?: return errorNotFound(trackNumber)
        return DbPDResponseOk(pdTable.from(res))
    }

    override suspend fun readPD(rq: DbPDIdRequest): IDbPDResponse = transactionWrapper { repd(rq.trackNumber) }

    private suspend fun update(
        trackNumber: PDParcelId,
        lock: PDLock,
        block: (PDParcel) -> IDbPDResponse
    ): IDbPDResponse =
        transactionWrapper {
            if (trackNumber == PDParcelId.NONE) return@transactionWrapper errorEmptyId

            val current = pdTable.selectAll().andWhere { pdTable.trackNumber eq trackNumber.asString() }
                .singleOrNull()
                ?.let { pdTable.from(it) }

            when {
                current == null -> errorNotFound(trackNumber)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }

    override suspend fun updatePD(rq: DbPDRequest): IDbPDResponse = update(rq.pd.trackNumber, rq.pd.lock) {
        val updated = pdTable.update(where = { pdTable.trackNumber eq rq.pd.trackNumber.asString() }) {
            it.to(rq.pd.copy(lock = PDLock(randomUuid())), randomUuid)
        }
        if (updated == 0) errorNotFound(rq.pd.trackNumber) else repd(rq.pd.trackNumber)
    }

    override suspend fun deletePD(rq: DbPDIdRequest): IDbPDResponse = update(rq.trackNumber, rq.lock) {
        pdTable.deleteWhere { trackNumber eq rq.trackNumber.asString() }
        DbPDResponseOk(it)
    }

    override suspend fun searchPD(rq: DbPDFilterRequest): IDbPDsResponse =
        transactionWrapper({
            val res = pdTable.selectAll().andWhere {
                buildList {
                    add(Op.TRUE)
                    if (rq.senderId != PDUserId.NONE) {
                        add(pdTable.senderId eq rq.senderId.asString())
                    }
                    if (rq.receiverId != PDUserId.NONE) {
                        add(pdTable.receiverId eq rq.receiverId.asString())
                    }
                    val status = rq.status
                    if (status != null && status != PDStatus.NONE) {
                        add(pdTable.status eq Cast(stringLiteral(status.name), PDStatusEnumType))
                    }
                }.reduce { a, b -> a and b }
            }
            DbPDsResponseOk(data = res.map { pdTable.from(it) })
        }, {
            DbPDsResponseErr(it.asPDError(code = "db-error"))
        })
}
