package ru.parceldelivery.app.spring.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
import ru.parceldelivery.api.v1.models.IRequest
import ru.parceldelivery.api.v1.models.IResponse
import ru.parceldelivery.api.v1.models.ParcelStatus
import ru.parceldelivery.api.v1.models.ParcelStatusUpdateRequest
import ru.parceldelivery.api.v1.models.ParcelSummary
import ru.parceldelivery.api.v1.models.RequestType
import ru.parceldelivery.api.v1.models.ResponseResult
import ru.parceldelivery.app.common.controllerHelper
import ru.parceldelivery.app.spring.base.PDAppSettings
import ru.parceldelivery.mappers.fromTransport
import ru.parceldelivery.mappers.toTransport
import ru.parceldelivery.mappers.toTransportDelete
import ru.parceldelivery.mappers.toTransportRead
import ru.parceldelivery.mappers.toTransportSearch
import ru.parceldelivery.mappers.toTransportUpdate
import kotlin.reflect.KClass

/**
 * REST API операций над посылками (v1).
 *
 * Командные операции (create/read/update/delete/search) выполняются POST-запросами
 * на подпути /v1/parcels/{command} с типизированным телом запроса,
 * REST-операции — на /v1/parcels и /v1/parcels/{trackNumber}.
 */
@RestController
@RequestMapping("v1/parcels")
class ParcelControllerV1(
    private val appSettings: PDAppSettings,
) {

    /**
     * POST /v1/parcels/create — регистрация новой посылки.
     */
    @PostMapping("create")
    suspend fun create(@RequestBody request: DeliveryCreateRequest): DeliveryCreateResponse =
        process(appSettings, request = request, this::class, "create")

    /**
     * POST /v1/parcels/read — чтение посылки по трек-номеру.
     */
    @PostMapping("read")
    suspend fun read(@RequestBody request: DeliveryReadRequest): DeliveryReadResponse =
        process(appSettings, request = request, this::class, "read")

    /**
     * POST /v1/parcels/update — обновление посылки (изменение статуса).
     */
    @PostMapping("update")
    suspend fun update(@RequestBody request: DeliveryUpdateRequest): DeliveryUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    /**
     * POST /v1/parcels/delete — удаление посылки по трек-номеру.
     */
    @PostMapping("delete")
    suspend fun delete(@RequestBody request: DeliveryDeleteRequest): DeliveryDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    /**
     * POST /v1/parcels/search — поиск посылок с фильтрацией.
     */
    @PostMapping("search")
    suspend fun search(@RequestBody request: DeliverySearchRequest): DeliverySearchResponse =
        process(appSettings, request = request, this::class, "search")

    /**
     * GET /v1/parcels — список посылок с фильтрацией
     * по необязательным параметрам status, senderId, receiverId.
     */
    @GetMapping
    suspend fun searchParcels(
        @RequestParam(required = false) status: ParcelStatus?,
        @RequestParam(required = false) senderId: String?,
        @RequestParam(required = false) receiverId: String?,
    ): List<ParcelSummary> =
        appSettings.controllerHelper(
            {
                fromTransport(
                    DeliverySearchRequest(
                        requestType = RequestType.SEARCH,
                        status = status,
                        senderId = senderId,
                        receiverId = receiverId,
                    )
                )
            },
            { toTransportSearch().parcels.orEmpty() },
            this::class,
            "searchParcels",
        )

    /**
     * GET /v1/parcels/{trackNumber} — полная информация о посылке.
     * При отсутствии посылки — 404 с телом IResponse.
     */
    @GetMapping("{trackNumber}")
    suspend fun readParcel(@PathVariable trackNumber: String): ResponseEntity<*> =
        appSettings.controllerHelper(
            { fromTransport(DeliveryReadRequest(requestType = RequestType.READ, trackNumber = trackNumber)) },
            {
                val response = toTransportRead()
                if (response.result == ResponseResult.ERROR) {
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
                } else {
                    ResponseEntity.ok(response.parcel)
                }
            },
            this::class,
            "readParcel",
        )

    /**
     * DELETE /v1/parcels/{trackNumber} — удаление посылки.
     * При отсутствии посылки — 404 с телом IResponse.
     */
    @DeleteMapping("{trackNumber}")
    suspend fun deleteParcel(@PathVariable trackNumber: String): ResponseEntity<out IResponse> =
        appSettings.controllerHelper(
            { fromTransport(DeliveryDeleteRequest(requestType = RequestType.DELETE, trackNumber = trackNumber)) },
            {
                val response = toTransportDelete()
                if (response.result == ResponseResult.ERROR) {
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
                } else {
                    ResponseEntity.ok(response)
                }
            },
            this::class,
            "deleteParcel",
        )

    /**
     * PATCH /v1/parcels/{trackNumber}/status — ручное изменение статуса посылки.
     * При отсутствии посылки — 404 с телом IResponse.
     */
    @PatchMapping("{trackNumber}/status")
    suspend fun updateParcelStatus(
        @PathVariable trackNumber: String,
        @RequestBody request: ParcelStatusUpdateRequest,
    ): ResponseEntity<out IResponse> =
        appSettings.controllerHelper(
            {
                fromTransport(
                    DeliveryUpdateRequest(
                        requestType = RequestType.UPDATE,
                        trackNumber = trackNumber,
                        status = request.status,
                    )
                )
            },
            {
                val response = toTransportUpdate()
                if (response.result == ResponseResult.ERROR) {
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
                } else {
                    ResponseEntity.ok(response)
                }
            },
            this::class,
            "updateParcelStatus",
        )

    companion object {
        @Suppress("UNCHECKED_CAST")
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: PDAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransport() as R },
            clazz,
            logId,
        )
    }
}