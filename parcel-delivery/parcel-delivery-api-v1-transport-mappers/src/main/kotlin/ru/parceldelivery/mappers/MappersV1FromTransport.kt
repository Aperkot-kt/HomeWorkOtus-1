package ru.parceldelivery.mappers

import ru.parceldelivery.api.v1.models.DeliveryCreateRequest
import ru.parceldelivery.api.v1.models.DeliveryDeleteRequest
import ru.parceldelivery.api.v1.models.DeliveryReadRequest
import ru.parceldelivery.api.v1.models.DeliverySearchRequest
import ru.parceldelivery.api.v1.models.DeliveryUpdateRequest
import ru.parceldelivery.api.v1.models.IRequest
import ru.parceldelivery.api.v1.models.ParcelDimensions
import ru.parceldelivery.api.v1.models.ParcelStatus
import ru.parceldelivery.exceptions.UnknownRequestClass
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelDimensions
import ru.parceldelovery.common.models.PDStatus
import ru.parceldelovery.common.models.PDFilter
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDUserId


private fun String?.toPDId() = this?.let { PDParcelId(it) } ?: PDParcelId.NONE

fun PDContext.fromTransport(request: IRequest) = when (request) {
    is DeliveryCreateRequest -> fromTransport(request)
    is DeliveryReadRequest -> fromTransport(request)
    is DeliveryUpdateRequest -> fromTransport(request)
    is DeliveryDeleteRequest -> fromTransport(request)
    is DeliverySearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

fun PDContext.fromTransport(request: DeliveryReadRequest) {
    command = PDCommand.READ
    pdRequest = PDParcel(trackNumber = request.trackNumber.toPDId())
}

fun PDContext.fromTransport(request: DeliveryCreateRequest) {
    command = PDCommand.CREATE
    pdRequest = request.toInternal()
}

fun PDContext.fromTransport(request: DeliveryUpdateRequest) {
    command = PDCommand.UPDATE
    pdRequest = PDParcel(
        trackNumber = request.trackNumber.toPDId(),
        status = request.status.fromTransport(),
        senderId = request.senderId?.let { PDUserId(it) } ?: PDUserId.NONE,
        receiverId = request.receiverId?.let { PDUserId(it) } ?: PDUserId.NONE,
    )
}

fun PDContext.fromTransport(request: DeliveryDeleteRequest) {
    command = PDCommand.DELETE
    pdRequest = PDParcel(trackNumber = request.trackNumber.toPDId())
}

fun PDContext.fromTransport(request: DeliverySearchRequest) {
    command = PDCommand.SEARCH
    pdFilterRequest = request.toInternal()
}

private fun DeliveryCreateRequest.toInternal() = PDParcel(
    trackNumber = PDParcelId.NONE,
    senderId = senderId?.let { PDUserId(it) } ?: PDUserId.NONE,
    receiverId = receiverId?.let { PDUserId(it) } ?: PDUserId.NONE,
    weight = weight,
    dimensions = dimensions.toInternal()
)

private fun DeliverySearchRequest.toInternal() = PDFilter(
    status = status.fromTransport(),
    senderId = senderId,
    receiverId = receiverId
)

private fun ParcelDimensions.toInternal() = PDParcelDimensions(
    length = length,
    width = width,
    height = height
)

private fun ParcelStatus?.fromTransport(): PDStatus = when (this) {
    ParcelStatus.PENDING_DELIVERY -> PDStatus.PENDING_DELIVERY
    ParcelStatus.IN_TRANSIT -> PDStatus.IN_TRANSIT
    ParcelStatus.ACCEPTED -> PDStatus.ACCEPTED
    ParcelStatus.DELIVERED -> PDStatus.DELIVERED
    null -> PDStatus.NONE
}