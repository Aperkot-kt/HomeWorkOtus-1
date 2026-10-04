package ru.parcel.delivery.backend.repo.tests

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelDimensions
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDLock
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId
import ru.parceldelivery.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPDUpdateTest {
    abstract val repo: IRepoPD
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = PDParcelId("pd-repo-update-not-found")
    protected val lockBad = PDLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = PDLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        PDParcel(
            trackNumber = updateSucc.trackNumber,
            senderId = updateSucc.senderId,
            receiverId = PDUserId("receiver-999"),
            weight = 2.5,
            dimensions = PDParcelDimensions(50.0, 40.0, 30.0),
            status = PDStatus.IN_TRANSIT,
            deliveryAddress = "update delivery address",
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = PDParcel(
        trackNumber = updateIdNotFound,
        status = PDStatus.IN_TRANSIT,
        deliveryAddress = "update object not found",
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        PDParcel(
            trackNumber = updateConc.trackNumber,
            status = PDStatus.IN_TRANSIT,
            deliveryAddress = "update object not found",
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updatePD(DbPDRequest(reqUpdateSucc))
        assertIs<DbPDResponseOk>(result)
        assertEquals(reqUpdateSucc.trackNumber, result.data.trackNumber)
        assertEquals(reqUpdateSucc.status, result.data.status)
        assertEquals(reqUpdateSucc.deliveryAddress, result.data.deliveryAddress)
        assertEquals(reqUpdateSucc.dimensions, result.data.dimensions)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updatePD(DbPDRequest(reqUpdateNotFound))
        assertIs<DbPDResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updatePD(DbPDRequest(reqUpdateConc))
        assertIs<DbPDResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitPDs("update") {
        override val initObjects: List<PDParcel> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
