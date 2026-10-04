package ru.parcel.delivery.backend.repo.tests

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoPDDeleteTest {
    abstract val repo: IRepoPD
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = PDParcelId("pd-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deletePD(DbPDIdRequest(deleteSucc.trackNumber, lock = lockOld))
        assertIs<DbPDResponseOk>(result)
        assertEquals(deleteSucc.trackNumber, result.data.trackNumber)
        assertEquals(deleteSucc.deliveryAddress, result.data.deliveryAddress)
        assertEquals(deleteSucc.status, result.data.status)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readPD(DbPDIdRequest(notFoundId, lock = lockOld))

        assertIs<DbPDResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deletePD(DbPDIdRequest(deleteConc.trackNumber, lock = lockBad))

        assertIs<DbPDResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitPDs("delete") {
        override val initObjects: List<PDParcel> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
