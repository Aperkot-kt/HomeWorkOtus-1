package ru.parceldelivery.mappers

import org.junit.jupiter.api.Test
import ru.parceldelivery.api.v1.models.DeliveryCreateResponse
import ru.parceldelivery.api.v1.models.DeliveryDeleteResponse
import ru.parceldelivery.api.v1.models.DeliveryReadResponse
import ru.parceldelivery.api.v1.models.DeliverySearchResponse
import ru.parceldelivery.api.v1.models.DeliveryUpdateResponse
import ru.parceldelivery.api.v1.models.ParcelStatus
import ru.parceldelivery.api.v1.models.ResponseResult
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.exception.UnknownContextCommand
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelDimensions
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDUserId
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.models.PDStatus
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull


class MappersV1ToTransportTest {

    private val createdAt = OffsetDateTime.parse("2026-06-30T10:00:00Z")

    private fun fullParcel(
        trackNumber: String = "PD-2026-000001",
        status: PDStatus = PDStatus.ACCEPTED
    ) = PDParcel(
        trackNumber = PDParcelId(trackNumber),
        senderId = PDUserId("CL-1001"),
        receiverId = PDUserId("CL-1002"),
        weight = 5.5,
        dimensions = PDParcelDimensions(length = 40.0, width = 30.0, height = 20.0),
        status = status,
        deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 10",
        createdAt = createdAt
    )

    @Test
    fun `create context maps to DeliveryCreateResponse with full parcel`() {
        val context = PDContext(command = PDCommand.CREATE, adResponse = fullParcel())

        val response = assertIs<DeliveryCreateResponse>(context.toTransport())

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertNull(response.errors)
        assertEquals("PD-2026-000001", response.trackNumber)

        val parcel = assertNotNull(response.parcel)
        assertEquals("PD-2026-000001", parcel.trackNumber)
        assertEquals("CL-1001", parcel.senderId)
        assertEquals("CL-1002", parcel.receiverId)
        assertEquals(5.5, parcel.weight)
        assertEquals(40.0, parcel.dimensions.length)
        assertEquals(30.0, parcel.dimensions.width)
        assertEquals(20.0, parcel.dimensions.height)
        assertEquals(ParcelStatus.ACCEPTED, parcel.status)
        assertEquals("г. Санкт-Петербург, ул. Невский пр., д. 10", parcel.deliveryAddress)
        assertEquals(createdAt, parcel.createdAt)
        assertNull(parcel.updatedAt)
    }

    @Test
    fun `read context maps to DeliveryReadResponse`() {
        val context = PDContext(command = PDCommand.READ, adResponse = fullParcel())

        val response = assertIs<DeliveryReadResponse>(context.toTransport())

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertNull(response.errors)
        assertEquals(ParcelStatus.ACCEPTED, assertNotNull(response.parcel).status)
        assertEquals("CL-1001", response.parcel?.senderId)
    }

    @Test
    fun `update context maps status to DeliveryUpdateResponse`() {
        val context = PDContext(command = PDCommand.UPDATE, adResponse = fullParcel(status = PDStatus.IN_TRANSIT))

        val response = assertIs<DeliveryUpdateResponse>(context.toTransport())

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals(ParcelStatus.IN_TRANSIT, assertNotNull(response.parcel).status)
    }

    @Test
    fun `delete context maps to DeliveryDeleteResponse`() {
        val context = PDContext(command = PDCommand.DELETE)

        val response = assertIs<DeliveryDeleteResponse>(context.toTransport())

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertNull(response.errors)
    }

    @Test
    fun `search context maps parcels to summaries`() {
        val context = PDContext(
            command = PDCommand.SEARCH,
            adsResponse = mutableListOf(
                fullParcel(trackNumber = "PD-2026-000001", status = PDStatus.ACCEPTED),
                fullParcel(trackNumber = "PD-2026-000002", status = PDStatus.IN_TRANSIT)
            )
        )

        val response = assertIs<DeliverySearchResponse>(context.toTransport())

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertNull(response.errors)

        val summaries = assertNotNull(response.parcels)
        assertEquals(2, summaries.size)

        val first = summaries[0]
        val second = summaries[1]
        assertEquals("PD-2026-000001", first.trackNumber)
        assertEquals(ParcelStatus.ACCEPTED, first.status)
        assertEquals("CL-1001", first.senderId)
        assertEquals("CL-1002", first.receiverId)
        assertEquals("г. Санкт-Петербург, ул. Невский пр., д. 10", first.deliveryAddress)
        assertEquals(createdAt, first.createdAt)
        assertEquals("PD-2026-000002", second.trackNumber)
        assertEquals(ParcelStatus.IN_TRANSIT, second.status)
    }

    @Test
    fun `search without results maps to empty list`() {
        val context = PDContext(command = PDCommand.SEARCH)

        val response = assertIs<DeliverySearchResponse>(context.toTransport())

        assertEquals(emptyList(), response.parcels)
        assertNull(response.errors)
    }

    @Test
    fun `context with errors maps to error result with mapped errors`() {
        val context = PDContext(command = PDCommand.READ, adResponse = fullParcel())
        context.errors += PDError(
            code = "NOT_FOUND",
            group = "delivery",
            field = "trackNumber",
            message = "Посылка не найдена"
        )

        val response = assertIs<DeliveryReadResponse>(context.toTransport())

        assertEquals(ResponseResult.ERROR, response.result)
        val errors = assertNotNull(response.errors)
        assertEquals(1, errors.size)
        assertEquals("NOT_FOUND", errors.first().code)
        assertEquals("delivery", errors.first().group)
        assertEquals("trackNumber", errors.first().field)
        assertEquals("Посылка не найдена", errors.first().message)
    }

    @Test
    fun `context without command is rejected`() {
        val context = PDContext()

        assertFailsWith<UnknownContextCommand> { context.toTransport() }
    }

    @Test
    fun `parcel without status is rejected`() {
        val context = PDContext(command = PDCommand.READ, adResponse = fullParcel().copy(status = null))

        assertFailsWith<IllegalStateException> { context.toTransport() }
    }

    @Test
    fun `parcel with NONE status is rejected`() {
        val context = PDContext(command = PDCommand.READ, adResponse = fullParcel().copy(status = PDStatus.NONE))

        assertFailsWith<IllegalStateException> { context.toTransport() }
    }

    @Test
    fun `parcel without senderId is rejected`() {
        val context = PDContext(command = PDCommand.READ, adResponse = fullParcel().copy(senderId = PDUserId.NONE))

        assertFailsWith<IllegalArgumentException> { context.toTransport() }
    }

    @Test
    fun `parcel without created at is rejected`() {
        val context = PDContext(command = PDCommand.READ, adResponse = fullParcel().copy(createdAt = null))

        assertFailsWith<IllegalArgumentException> { context.toTransport() }
    }
}