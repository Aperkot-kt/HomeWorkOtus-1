package ru.parceldelovery.common.models

import java.time.OffsetDateTime

/**
 * Полная информация о посылке
 */
data class PDParcel(
    /** Уникальный трек-номер посылки (генерируется системой) */
    val trackNumber: PDParcelId = PDParcelId.NONE,
    /** Идентификатор клиента-отправителя */
    val senderId: PDUserId = PDUserId.NONE,
    /** Идентификатор клиента-получателя */
    val receiverId: PDUserId = PDUserId.NONE,
    /** Вес посылки в кг */
    val weight: Double? = null,
    val dimensions: PDParcelDimensions? = null,
    val status: PDStatus? = null,
    /** Адрес доставки (адрес получателя) */
    val deliveryAddress: String = "",
    /** Дата и время создания посылки */
    val createdAt: OffsetDateTime? = null,
    /** Дата и время последнего изменения */
    val updatedAt: OffsetDateTime? = null,
    /** Блокировка */
    var lock: PDLock = PDLock.NONE,

    ) {

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = PDParcel()
    }
}