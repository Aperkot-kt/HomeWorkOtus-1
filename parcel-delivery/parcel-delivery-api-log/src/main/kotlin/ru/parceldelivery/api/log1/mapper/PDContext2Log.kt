package ru.parceldelivery.api.log.v1.mapper

import kotlinx.datetime.Clock
import ru.parceldelivery.api.log.v1.models.CommonLogModel
import ru.parceldelivery.api.log.v1.models.ErrorLogModel
import ru.parceldelivery.api.log.v1.models.PDLogModel
import ru.parceldelivery.api.log.v1.models.ParcelFilterLog
import ru.parceldelivery.api.log.v1.models.ParcelLog
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.models.PDFilter
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDRequestId
import ru.parceldelovery.common.models.PDUserId

fun PDContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "parcel-delivery-app",
    parcel = toPDLog(),
    errors = errors.map { it.toLog() },
)

private fun PDContext.toPDLog(): PDLogModel? {
    val pdNone = PDParcel()
    return PDLogModel(
        requestId = requestId.takeIf { it != PDRequestId.NONE }?.asString(),
        requestParcel = pdRequest.takeIf { it != pdNone }?.toLog(),
        responseParcel = pdResponse.takeIf { it != pdNone }?.toLog(),
        responseParcels = pdsResponse.takeIf { it.isNotEmpty() }?.filter { it != pdNone }?.map { it.toLog() },
        requestFilter = pdFilterRequest.takeIf { !it.isEmpty() }?.toLog(),
    ).takeIf { it != PDLogModel() }
}

private fun PDFilter.toLog() = ParcelFilterLog(
    status = status?.name,
    senderId = senderId?.takeIf { it.isNotBlank() },
    receiverId = receiverId?.takeIf { it.isNotBlank() },
)

private fun PDError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun PDParcel.toLog() = ParcelLog(
    trackNumber = trackNumber.takeIf { it != PDParcelId.NONE }?.asString(),
    senderId = senderId.takeIf { it != PDUserId.NONE }?.asString(),
    receiverId = receiverId.takeIf { it != PDUserId.NONE }?.asString(),
    weight = weight,
    status = status?.name,
    deliveryAddress = deliveryAddress.takeIf { it.isNotBlank() },
    createdAt = createdAt?.toString(),
    updatedAt = updatedAt?.toString(),
)