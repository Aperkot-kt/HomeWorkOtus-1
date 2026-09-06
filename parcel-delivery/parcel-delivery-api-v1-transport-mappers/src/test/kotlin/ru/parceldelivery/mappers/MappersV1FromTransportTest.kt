package ru.parceldelivery.mappers

import org.junit.jupiter.api.Test
import ru.parceldelivery.api.v1.models.DeliveryCreateRequest
import ru.parceldelivery.api.v1.models.DeliveryDeleteRequest
import ru.parceldelivery.api.v1.models.DeliveryReadRequest
import ru.parceldelivery.api.v1.models.DeliverySearchRequest
import ru.parceldelivery.api.v1.models.DeliveryUpdateRequest
import ru.parceldelivery.api.v1.models.IRequest
import ru.parceldelivery.api.v1.models.ParcelDimensions
import ru.parceldelivery.api.v1.models.ParcelStatus
import ru.parceldelivery.api.v1.models.RequestType
import ru.parceldelivery.exceptions.UnknownRequestClass
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDStatus
import ru.parceldelovery.common.models.PDUserId
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MappersV1FromTransportTest {

    private val senderId = "CL-1001"

    private val receiverId = "CL-1002"

    private val dimensions = ParcelDimensions(length = 40.0, width = 30.0, height = 20.0)

    private val trackNumber = "PD-2026-000001"

    @Test
    fun `read request maps to READ command with track number`() {
        val context = PDContext().apply {
            fromTransport(DeliveryReadRequest(requestType = RequestType.READ, trackNumber = trackNumber))
        }

        assertEquals(PDCommand.READ, context.command)
        assertEquals(PDParcelId(trackNumber), context.adRequest.trackNumber)
    }

    @Test
    fun `create request maps to CREATE command with full parcel`() {
        val context = PDContext().apply {
            fromTransport(
                DeliveryCreateRequest(
                    requestType = RequestType.CREATE,
                    senderId = senderId,
                    receiverId = receiverId,
                    weight = 5.5,
                    dimensions = dimensions
                )
            )
        }

        assertEquals(PDCommand.CREATE, context.command)

        val parcel = context.adRequest
        assertEquals(PDParcelId.NONE, parcel.trackNumber)
        assertEquals(PDUserId(senderId), parcel.senderId)
        assertEquals(PDUserId(receiverId), parcel.receiverId)
        assertEquals(5.5, parcel.weight)
        assertEquals(40.0, parcel.dimensions?.length)
        assertEquals(30.0, parcel.dimensions?.width)
        assertEquals(20.0, parcel.dimensions?.height)
        assertNull(parcel.status)
    }

    @Test
    fun `update request maps to UPDATE command with track number and status`() {
        val context = PDContext().apply {
            fromTransport(
                DeliveryUpdateRequest(
                    requestType = RequestType.UPDATE,
                    trackNumber = trackNumber,
                    status = ParcelStatus.IN_TRANSIT
                )
            )
        }

        assertEquals(PDCommand.UPDATE, context.command)
        assertEquals(PDParcelId(trackNumber), context.adRequest.trackNumber)
        assertEquals(PDStatus.IN_TRANSIT, context.adRequest.status)
    }

    @Test
    fun `delete request maps to DELETE command with track number`() {
        val context = PDContext().apply {
            fromTransport(DeliveryDeleteRequest(requestType = RequestType.DELETE, trackNumber = trackNumber))
        }

        assertEquals(PDCommand.DELETE, context.command)
        assertEquals(PDParcelId(trackNumber), context.adRequest.trackNumber)
    }

    @Test
    fun `search request maps to SEARCH command and fills filter`() {
        val context = PDContext().apply {
            fromTransport(
                DeliverySearchRequest(
                    requestType = RequestType.SEARCH,
                    status = ParcelStatus.IN_TRANSIT,
                    senderId = senderId,
                    receiverId = receiverId
                )
            )
        }

        assertEquals(PDCommand.SEARCH, context.command)
        assertEquals(PDStatus.IN_TRANSIT, context.adFilterRequest.status)
        assertEquals(senderId, context.adFilterRequest.senderId)
        assertEquals(receiverId, context.adFilterRequest.receiverId)
    }

    @Test
    fun `search request without optional fields maps to empty filter`() {
        val context = PDContext().apply {
            fromTransport(DeliverySearchRequest(requestType = RequestType.SEARCH))
        }

        assertEquals(PDCommand.SEARCH, context.command)
        assertEquals(PDStatus.NONE, context.adFilterRequest.status)
        assertNull(context.adFilterRequest.senderId)
        assertNull(context.adFilterRequest.receiverId)
    }

    @Test
    fun `unknown request class is rejected`() {
        val context = PDContext()

        assertFailsWith<UnknownRequestClass> {
            context.fromTransport(UnknownRequest(requestType = RequestType.READ))
        }
    }

    private class UnknownRequest(override val requestType: RequestType?) : IRequest
}