package ru.parceldelivery.mappers

import org.junit.jupiter.api.Test
import ru.parceldelivery.api.v1.models.DeliveryCreateResponse
import ru.parceldelivery.api.v1.models.DeliveryDeleteResponse
import ru.parceldelivery.api.v1.models.DeliveryInitResponse
import ru.parceldelivery.api.v1.models.DeliveryReadResponse
import ru.parceldelivery.api.v1.models.DeliverySearchResponse
import ru.parceldelivery.api.v1.models.DeliveryUpdateResponse
import ru.parceldelivery.api.v1.models.IResponse
import ru.parceldelivery.api.v1.models.Parcel
import ru.parceldelivery.api.v1.models.ParcelDimensions
import ru.parceldelivery.api.v1.models.ParcelStatus
import ru.parceldelivery.api.v1.models.ParcelSummary
import ru.parceldelivery.api.v1.models.ResponseResult

import kotlin.test.assertEquals

class ResponseSerializationTest {

    val senderId = "CL-1001"

    val receiverId = "CL-1002"

    val dimensions = ParcelDimensions(
        length = 40.0,
        width = 30.0,
        height = 20.0
    )

    val parcel = Parcel(
        trackNumber = "PD-2026-000001",
        senderId = senderId,
        receiverId = receiverId,
        weight = 5.5,
        dimensions = dimensions,
        status = ParcelStatus.ACCEPTED,
        deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 10",
        createdAt = "2026-06-30T10:00:00Z"
    )

    val response = DeliveryCreateResponse(
        responseType = "create",
        result = ResponseResult.SUCCESS,
        trackNumber = "PD-2026-000001",
        parcel = parcel
    )

    val readResponse = DeliveryReadResponse(
        responseType = "read",
        result = ResponseResult.SUCCESS,
        parcel = parcel
    )

    val updateResponse = DeliveryUpdateResponse(
        responseType = "update",
        result = ResponseResult.SUCCESS,
        parcel = parcel.copy(status = ParcelStatus.IN_TRANSIT, updatedAt = "2026-06-30T15:00:00Z")
    )

    val deleteResponse = DeliveryDeleteResponse(
        responseType = "delete",
        result = ResponseResult.SUCCESS
    )

    val parcelSummary = ParcelSummary(
        trackNumber = "PD-2026-000001",
        senderId = senderId,
        receiverId = receiverId,
        deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 10",
        status = ParcelStatus.IN_TRANSIT,
        createdAt = "2026-06-30T10:00:00Z"
    )

    val searchResponse = DeliverySearchResponse(
        responseType = "search",
        result = ResponseResult.SUCCESS,
        parcels = listOf(parcelSummary)
    )

    val initResponse = DeliveryInitResponse(
        responseType = "init",
        result = ResponseResult.SUCCESS,
        version = "1.0.0",
        status = "running"
    )

    @Test
    fun `serialize delivery create response`() {

        val json = apiV1Mapper.writeValueAsString(response)

        assert(json.contains("\"responseType\":\"create\""))
        assert(json.contains("\"result\":\"success\""))
        assert(json.contains("\"trackNumber\":\"PD-2026-000001\""))
        assert(json.contains("\"status\":\"ACCEPTED\""))
    }

    @Test
    fun `deserialize delivery create response`() {

        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DeliveryCreateResponse

        assertEquals(response, obj)

    }

    @Test
    fun `serialize delivery read response`() {

        val json = apiV1Mapper.writeValueAsString(readResponse)

        assert(json.contains("\"responseType\":\"read\""))
        assert(json.contains("\"result\":\"success\""))
        assert(json.contains("\"trackNumber\":\"PD-2026-000001\""))
        assert(json.contains("\"status\":\"ACCEPTED\""))
    }

    @Test
    fun `deserialize delivery read response`() {

        val json = apiV1Mapper.writeValueAsString(readResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DeliveryReadResponse

        assertEquals(readResponse, obj)

    }

    @Test
    fun `serialize delivery update response`() {

        val json = apiV1Mapper.writeValueAsString(updateResponse)

        assert(json.contains("\"responseType\":\"update\""))
        assert(json.contains("\"result\":\"success\""))
        assert(json.contains("\"status\":\"IN_TRANSIT\""))
        assert(json.contains("\"updatedAt\":\"2026-06-30T15:00:00Z\""))
    }

    @Test
    fun `deserialize delivery update response`() {

        val json = apiV1Mapper.writeValueAsString(updateResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DeliveryUpdateResponse

        assertEquals(updateResponse, obj)

    }

    @Test
    fun `serialize delivery delete response`() {

        val json = apiV1Mapper.writeValueAsString(deleteResponse)

        assert(json.contains("\"responseType\":\"delete\""))
        assert(json.contains("\"result\":\"success\""))
    }

    @Test
    fun `deserialize delivery delete response`() {

        val json = apiV1Mapper.writeValueAsString(deleteResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DeliveryDeleteResponse

        assertEquals(deleteResponse.responseType, obj.responseType)
        assertEquals(deleteResponse.result, obj.result)

    }

    @Test
    fun `serialize delivery search response`() {

        val json = apiV1Mapper.writeValueAsString(searchResponse)

        assert(json.contains("\"responseType\":\"search\""))
        assert(json.contains("\"result\":\"success\""))
        assert(json.contains("\"status\":\"IN_TRANSIT\""))
        assert(json.contains("\"senderId\":\"CL-1001\""))
    }

    @Test
    fun `deserialize delivery search response`() {

        val json = apiV1Mapper.writeValueAsString(searchResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DeliverySearchResponse

        assertEquals(searchResponse, obj)

    }

    @Test
    fun `serialize delivery init response`() {

        val json = apiV1Mapper.writeValueAsString(initResponse)

        assert(json.contains("\"responseType\":\"init\""))
        assert(json.contains("\"result\":\"success\""))
        assert(json.contains("\"version\":\"1.0.0\""))
        assert(json.contains("\"status\":\"running\""))
    }

    @Test
    fun `deserialize delivery init response`() {

        val json = apiV1Mapper.writeValueAsString(initResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DeliveryInitResponse

        assertEquals(initResponse, obj)

    }

}
