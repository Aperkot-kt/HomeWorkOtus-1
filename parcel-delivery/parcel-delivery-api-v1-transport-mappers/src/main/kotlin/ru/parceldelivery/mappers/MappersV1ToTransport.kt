package ru.parceldelivery.mappers

import ru.parceldelivery.api.v1.models.DeliveryCreateResponse
import ru.parceldelivery.api.v1.models.DeliveryDeleteResponse
import ru.parceldelivery.api.v1.models.DeliveryReadResponse
import ru.parceldelivery.api.v1.models.DeliverySearchResponse
import ru.parceldelivery.api.v1.models.DeliveryUpdateResponse
import ru.parceldelivery.api.v1.models.Error
import ru.parceldelivery.api.v1.models.IResponse
import ru.parceldelivery.api.v1.models.Parcel
import ru.parceldelivery.api.v1.models.ParcelDimensions
import ru.parceldelivery.api.v1.models.ParcelStatus
import ru.parceldelivery.api.v1.models.ParcelSummary
import ru.parceldelivery.api.v1.models.ResponseResult
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.exception.UnknownContextCommand
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelDimensions
import ru.parceldelovery.common.models.PDStatus
import ru.parceldelovery.common.models.PDUserId


fun PDContext.toTransport(): IResponse = when (command) {
    PDCommand.CREATE -> toTransportCreate()
    PDCommand.READ -> toTransportRead()
    PDCommand.UPDATE -> toTransportUpdate()
    PDCommand.DELETE -> toTransportDelete()
    PDCommand.SEARCH -> toTransportSearch()
    PDCommand.NONE -> throw UnknownContextCommand(command)
}

fun PDContext.toTransportCreate(): DeliveryCreateResponse = DeliveryCreateResponse(
    result = result,
    errors = transportErrors,
    trackNumber = pdResponse.trackNumber.toString(),
    parcel = pdResponse.toTransport()
)

fun PDContext.toTransportRead(): DeliveryReadResponse = DeliveryReadResponse(
    result = result,
    errors = transportErrors,
    parcel = pdResponse.toTransport()
)

fun PDContext.toTransportUpdate(): DeliveryUpdateResponse = DeliveryUpdateResponse(
    result = result,
    errors = transportErrors,
    parcel = pdResponse.toTransport()
)

fun PDContext.toTransportDelete(): DeliveryDeleteResponse = DeliveryDeleteResponse(
    result = result,
    errors = transportErrors
)

fun PDContext.toTransportSearch(): DeliverySearchResponse = DeliverySearchResponse(
    result = result,
    errors = transportErrors,
    parcels = pdsResponse.map { it.toTransportSummary() }
)

private val PDContext.result: ResponseResult
    get() = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR

private val PDContext.transportErrors: List<Error>?
    get() = errors.toTransport().ifEmpty { null }

private fun List<PDError>.toTransport(): List<Error> = map {
    Error(
        code = it.code,
        message = it.message,
        group = it.group,
        field = it.field
    )
}

private fun PDParcel.toTransport() = Parcel(
    trackNumber = trackNumber.toString(),
    senderId = requireNotNull(senderId.takeIf { it != PDUserId.NONE }?.asString()) { "senderId is not set for parcel $trackNumber" },
    receiverId = requireNotNull(receiverId.takeIf { it != PDUserId.NONE }?.asString()) { "receiverId is not set for parcel $trackNumber" },
    weight = requireNotNull(weight) { "weight is not set for parcel $trackNumber" },
    dimensions = requireNotNull(dimensions) { "dimensions are not set for parcel $trackNumber" }.toTransport(),
    status = status.toTransport(trackNumber.toString()),
    deliveryAddress = deliveryAddress,
    createdAt = requireNotNull(createdAt) { "createdAt is not set for parcel $trackNumber" },
    updatedAt = updatedAt
)

private fun PDParcel.toTransportSummary() = ParcelSummary(
    trackNumber = trackNumber.toString(),
    senderId = requireNotNull(senderId.takeIf { it != PDUserId.NONE }?.asString()) { "senderId is not set for parcel $trackNumber" },
    receiverId = requireNotNull(receiverId.takeIf { it != PDUserId.NONE }?.asString()) { "receiverId is not set for parcel $trackNumber" },
    deliveryAddress = deliveryAddress,
    status = status.toTransport(trackNumber.toString()),
    createdAt = createdAt
)

private fun PDParcelDimensions.toTransport() = ParcelDimensions(
    length = length,
    width = width,
    height = height
)

private fun PDStatus?.toTransport(trackNumber: String): ParcelStatus = when (this) {
    PDStatus.PENDING_DELIVERY -> ParcelStatus.PENDING_DELIVERY
    PDStatus.IN_TRANSIT -> ParcelStatus.IN_TRANSIT
    PDStatus.ACCEPTED -> ParcelStatus.ACCEPTED
    PDStatus.DELIVERED -> ParcelStatus.DELIVERED
    PDStatus.NONE -> throw IllegalStateException("status is not set for parcel $trackNumber")
    null -> throw IllegalStateException("status is not set for parcel $trackNumber")
}