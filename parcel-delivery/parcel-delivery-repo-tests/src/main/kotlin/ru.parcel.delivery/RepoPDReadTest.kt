package ru.parcel.delivery.backend.repo.tests

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.repo.DbPDIdRequest
import ru.parceldelivery.common.repo.DbPDResponseErr
import ru.parceldelivery.common.repo.DbPDResponseOk
import ru.parceldelivery.common.repo.IRepoPD
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoPDReadTest {
    abstract val repo: IRepoPD
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readPD(DbPDIdRequest(readSucc.trackNumber))

        assertIs<DbPDResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readPD(DbPDIdRequest(notFoundId))

        assertIs<DbPDResponseErr>(result)
        val error: PDError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitPDs("read") {
        override val initObjects: List<PDParcel> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = PDParcelId("pd-repo-read-notFound")
    }
}
