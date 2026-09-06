package ru.parceldelivery.app.spring.controllers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import ru.parceldelivery.api.v1.models.DeliveryCreateRequest
import ru.parceldelivery.api.v1.models.DeliveryCreateResponse
import ru.parceldelivery.api.v1.models.DeliveryDeleteRequest
import ru.parceldelivery.api.v1.models.DeliveryDeleteResponse
import ru.parceldelivery.api.v1.models.DeliveryReadRequest
import ru.parceldelivery.api.v1.models.DeliveryReadResponse
import ru.parceldelivery.api.v1.models.DeliverySearchRequest
import ru.parceldelivery.api.v1.models.DeliverySearchResponse
import ru.parceldelivery.api.v1.models.DeliveryUpdateRequest
import ru.parceldelivery.api.v1.models.DeliveryUpdateResponse
import ru.parceldelivery.api.v1.models.ParcelDimensions
import ru.parceldelivery.api.v1.models.ParcelStatus
import ru.parceldelivery.api.v1.models.ParcelStatusUpdateRequest
import ru.parceldelivery.api.v1.models.ParcelSummary
import ru.parceldelivery.api.v1.models.RequestType
import ru.parceldelivery.api.v1.models.ResponseResult
import ru.parceldelivery.app.spring.config.ParcelConfig

@WebFluxTest(ParcelControllerV1::class, ParcelConfig::class)
internal class ParcelControllerV1Test {

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createParcel() {
        webClient.post().uri("/v1/parcels/create")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                DeliveryCreateRequest(
                    requestType = RequestType.CREATE,
                    senderId = "CL-1001",
                    receiverId = "CL-1002",
                    weight = 5.5,
                    dimensions = ParcelDimensions(length = 40.0, width = 30.0, height = 20.0),
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(DeliveryCreateResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("create")
                assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
                assertThat(response.errors).isNull()
                assertThat(response.trackNumber).startsWith("PD-")
                assertThat(response.parcel).isNotNull
                assertThat(response.parcel!!.trackNumber).isEqualTo(response.trackNumber)
                assertThat(response.parcel!!.status).isEqualTo(ParcelStatus.ACCEPTED)
                assertThat(response.parcel!!.weight).isEqualTo(5.5)
            }
    }

    @Test
    fun readParcelByCommand() {
        webClient.post().uri("/v1/parcels/read")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                DeliveryReadRequest(
                    requestType = RequestType.READ,
                    trackNumber = "PD-2026-000001",
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(DeliveryReadResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("read")
                assertThat(response.result).isEqualTo(ResponseResult.ERROR)
                assertThat(response.errors).isNotNull
                assertThat(response.errors!![0].code).isEqualTo("NOT_FOUND")
            }
    }

    @Test
    fun updateParcelByCommand() {
        webClient.post().uri("/v1/parcels/update")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                DeliveryUpdateRequest(
                    requestType = RequestType.UPDATE,
                    trackNumber = "PD-2026-000001",
                    status = ParcelStatus.IN_TRANSIT,
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(DeliveryUpdateResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("update")
                assertThat(response.result).isEqualTo(ResponseResult.ERROR)
                assertThat(response.errors).isNotNull
                assertThat(response.errors!![0].code).isEqualTo("NOT_FOUND")
            }
    }

    @Test
    fun deleteParcelByCommand() {
        webClient.post().uri("/v1/parcels/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                DeliveryDeleteRequest(
                    requestType = RequestType.DELETE,
                    trackNumber = "PD-2026-000001",
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(DeliveryDeleteResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("delete")
                assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
                assertThat(response.errors).isNull()
            }
    }

    @Test
    fun searchParcelsByCommand() {
        webClient.post().uri("/v1/parcels/search")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                DeliverySearchRequest(
                    requestType = RequestType.SEARCH,
                    status = ParcelStatus.IN_TRANSIT,
                    senderId = "CL-1001",
                    receiverId = "CL-1002",
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(DeliverySearchResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("search")
                assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
                assertThat(response.parcels).isNotNull
                assertThat(response.parcels).isEmpty()
            }
    }

    @Test
    fun searchParcels() {
        webClient.get().uri("/v1/parcels?status=IN_TRANSIT&senderId=CL-1001")
            .exchange()
            .expectStatus().isOk
            .expectBody(object : ParameterizedTypeReference<List<ParcelSummary>>() {})
            .value { parcels ->
                assertThat(parcels).isEmpty()
            }
    }

    @Test
    fun readParcelReturnsNotFound() {
        webClient.get().uri("/v1/parcels/PD-2026-000001")
            .exchange()
            .expectStatus().isNotFound
            .expectBody(DeliveryReadResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("read")
                assertThat(response.result).isEqualTo(ResponseResult.ERROR)
                assertThat(response.errors).isNotNull
                assertThat(response.errors!![0].code).isEqualTo("NOT_FOUND")
            }
    }

    @Test
    fun deleteParcel() {
        webClient.delete().uri("/v1/parcels/PD-2026-000001")
            .exchange()
            .expectStatus().isOk
            .expectBody(DeliveryDeleteResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("delete")
                assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
            }
    }

    @Test
    fun updateParcelStatusReturnsNotFound() {
        webClient.patch().uri("/v1/parcels/PD-2026-000001/status")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ParcelStatusUpdateRequest(status = ParcelStatus.IN_TRANSIT))
            .exchange()
            .expectStatus().isNotFound
            .expectBody(DeliveryUpdateResponse::class.java)
            .value { response ->
                assertThat(response.responseType).isEqualTo("update")
                assertThat(response.result).isEqualTo(ResponseResult.ERROR)
                assertThat(response.errors).isNotNull
                assertThat(response.errors!![0].code).isEqualTo("NOT_FOUND")
            }
    }
}