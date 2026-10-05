package ru.parcel.delivery.repo.inmemory

import ru.parceldelivery.common.models.PDLock
import ru.parceldelivery.common.models.PDUserId
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelDimensions
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDStatus
import java.time.ZoneOffset
import java.util.Date

data class PDEntity(
    /** Уникальный трек-номер посылки (генерируется системой) */
    val trackNumber: String? = null,
    /** Идентификатор клиента-отправителя */
    val senderId: String? = null,
    /** Идентификатор клиента-получателя */
    val receiverId: String? = null,
    /** Вес посылки в кг */
    val weight: Double = 0.0,
    val dimensions: String? = null,
    val status: String? = null,
    /** Адрес доставки (адрес получателя) */
    val deliveryAddress: String? = null,
    /** Дата и время создания посылки */
    val createdAt: Date? = null,
    /** Дата и время последнего изменения */
    val updatedAt: Date? = null,
    /** Блокировка */
    var lock: String? = null,

    ) {
    constructor(model: PDParcel): this(
        trackNumber = model.trackNumber.asString().takeIf { it.isNotBlank() },
        senderId = model.senderId.asString().takeIf { it.isNotBlank() },
        receiverId = model.receiverId.asString().takeIf { it.isNotBlank() },
        weight = model.weight,
        dimensions = model.dimensions?.let { "${it.length}x${it.width}x${it.height}" },
        status = model.status?.name,
        deliveryAddress = model.deliveryAddress.takeIf { it.isNotBlank() },
        createdAt = model.createdAt?.let { Date.from(it.toInstant()) },
        updatedAt = model.updatedAt?.let { Date.from(it.toInstant()) },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = PDParcel(
        trackNumber = trackNumber?.let { PDParcelId(it) } ?: PDParcelId.NONE,
        senderId = senderId?.let { PDUserId(it) } ?: PDUserId.NONE,
        receiverId = receiverId?.let { PDUserId(it) } ?: PDUserId.NONE,
        weight = weight,
        dimensions = dimensions?.let {
            val (length, width, height) = it.split("x").map { value -> value.toDouble() }
            PDParcelDimensions(length, width, height)
        },
        status = status?.let { PDStatus.valueOf(it) },
        deliveryAddress = deliveryAddress ?: "",
        createdAt = createdAt?.toInstant()?.atOffset(ZoneOffset.UTC),
        updatedAt = updatedAt?.toInstant()?.atOffset(ZoneOffset.UTC),
        lock = lock?.let { PDLock(it) } ?: PDLock.NONE,
    )
}
