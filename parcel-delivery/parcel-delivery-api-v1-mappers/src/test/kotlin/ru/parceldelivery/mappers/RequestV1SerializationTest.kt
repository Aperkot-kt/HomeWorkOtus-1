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
import kotlin.test.assertEquals

class RequestSerializationTest {

    val senderId = "CL-1001"

    val receiverId = "CL-1002"

    val dimensions = ParcelDimensions(
        length = 40.0,
        width = 30.0,
        height = 20.0
    )

    val request = DeliveryCreateRequest(
        requestType = RequestType.CREATE,
        senderId = senderId,
        receiverId = receiverId,
        weight = 5.5,
        dimensions = dimensions
    )

    val trackNumber = "PD-2026-000001"

    val readRequest = DeliveryReadRequest(
        requestType = RequestType.READ,
        trackNumber = trackNumber
    )

    val updateRequest = DeliveryUpdateRequest(
        requestType = RequestType.UPDATE,
        trackNumber = trackNumber,
        status = ParcelStatus.IN_TRANSIT
    )

    val deleteRequest = DeliveryDeleteRequest(
        requestType = RequestType.DELETE,
        trackNumber = trackNumber
    )

    val searchRequest = DeliverySearchRequest(
        requestType = RequestType.SEARCH,
        status = ParcelStatus.IN_TRANSIT,
        senderId = senderId,
        receiverId = receiverId
    )

    @Test
    fun `serialize delivery create request`() {

        val json = apiV1Mapper.writeValueAsString(request)

        assert(json.contains("\"requestType\":\"create\""))
        assert(json.contains("\"senderId\":\"CL-1001\""))
        assert(json.contains("\"weight\":5.5"))
    }

    @Test
    fun `deserialize delivery create request`() {

        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DeliveryCreateRequest

        assertEquals(request, obj)

    }

    @Test
    fun `serialize delivery read request`() {

        val json = apiV1Mapper.writeValueAsString(readRequest)

        assert(json.contains("\"requestType\":\"read\""))
        assert(json.contains("\"trackNumber\":\"$trackNumber\""))
    }

    @Test
    fun `deserialize delivery read request`() {

        val json = apiV1Mapper.writeValueAsString(readRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DeliveryReadRequest

        assertEquals(readRequest, obj)

    }

    @Test
    fun `serialize delivery update request`() {

        val json = apiV1Mapper.writeValueAsString(updateRequest)

        assert(json.contains("\"requestType\":\"update\""))
        assert(json.contains("\"trackNumber\":\"$trackNumber\""))
        assert(json.contains("\"status\":\"IN_TRANSIT\""))
    }

    @Test
    fun `deserialize delivery update request`() {

        val json = apiV1Mapper.writeValueAsString(updateRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DeliveryUpdateRequest

        assertEquals(updateRequest, obj)

    }

    @Test
    fun `serialize delivery delete request`() {

        val json = apiV1Mapper.writeValueAsString(deleteRequest)

        assert(json.contains("\"requestType\":\"delete\""))
        assert(json.contains("\"trackNumber\":\"$trackNumber\""))
    }

    @Test
    fun `deserialize delivery delete request`() {

        val json = apiV1Mapper.writeValueAsString(deleteRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DeliveryDeleteRequest

        assertEquals(deleteRequest, obj)

    }

    @Test
    fun `serialize delivery search request`() {

        val json = apiV1Mapper.writeValueAsString(searchRequest)

        assert(json.contains("\"requestType\":\"search\""))
        assert(json.contains("\"status\":\"IN_TRANSIT\""))
        assert(json.contains("\"senderId\":\"$senderId\""))
        assert(json.contains("\"receiverId\":\"$receiverId\""))
    }

    @Test
    fun `deserialize delivery search request`() {

        val json = apiV1Mapper.writeValueAsString(searchRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DeliverySearchRequest

        assertEquals(searchRequest, obj)

    }

}