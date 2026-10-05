package ru.parcel.delivery.backend.repo.tests

import ru.parcel.delivery.repo.common.IRepoPDInitializable
import ru.parceldelivery.common.models.*
import ru.parceldelivery.common.repo.DbPDRequest
import ru.parceldelivery.common.repo.DbPDResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertIs


abstract class RepoPDCreateTest {
    abstract val repo: IRepoPDInitializable
    protected open val uuidNew = PDParcelId("10000000-0000-0000-0000-000000000001")

    private val createObj = PDParcel(
        senderId = PDUserId("sender-123"),
        receiverId = PDUserId("receiver-124"),
        weight = 1.5,
        dimensions = PDParcelDimensions(40.0, 30.0, 20.0),
        status = PDStatus.ACCEPTED,
        deliveryAddress = "create delivery address",
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createPD(DbPDRequest(createObj))
        val expected = createObj
        assertIs<DbPDResponseOk>(result)
        assertNotEquals(PDParcelId.NONE, result.data.trackNumber)
        assertEquals(uuidNew.asString(), result.data.lock.asString())
        assertEquals(expected.senderId, result.data.senderId)
        assertEquals(expected.receiverId, result.data.receiverId)
        assertEquals(expected.status, result.data.status)
        assertNotEquals(PDParcelId.NONE, result.data.trackNumber)
    }

    companion object : BaseInitPDs("create") {
        override val initObjects: List<PDParcel> = emptyList()
    }
}
