package ru.parcel.delivery.backend.repo.tests

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId
import ru.parceldelivery.common.repo.DbPDFilterRequest
import ru.parceldelivery.common.repo.DbPDsResponseOk
import ru.parceldelivery.common.repo.IRepoPD
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPDSearchTest {
    abstract val repo: IRepoPD

    protected open val initializedObjects: List<PDParcel> = initObjects

    @Test
    fun searchSender() = runRepoTest {
        val result = repo.searchPD(DbPDFilterRequest(senderId = searchSenderId))
        assertIs<DbPDsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.trackNumber.asString() }
        assertEquals(expected, result.data.sortedBy { it.trackNumber.asString() })
    }

    @Test
    fun searchStatus() = runRepoTest {
        val result = repo.searchPD(DbPDFilterRequest(status = PDStatus.IN_TRANSIT))
        assertIs<DbPDsResponseOk>(result)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.trackNumber.asString() }
        assertEquals(expected, result.data.sortedBy { it.trackNumber.asString() })
    }

    companion object: BaseInitPDs("search") {

        val searchSenderId = PDUserId("sender-999")
        override val initObjects: List<PDParcel> = listOf(
            createInitTestModel("pd1"),
            createInitTestModel("pd2", senderId = searchSenderId),
            createInitTestModel("pd3", status = PDStatus.IN_TRANSIT),
            createInitTestModel("pd4", senderId = searchSenderId),
            createInitTestModel("pd5", status = PDStatus.IN_TRANSIT),
        )
    }
}
