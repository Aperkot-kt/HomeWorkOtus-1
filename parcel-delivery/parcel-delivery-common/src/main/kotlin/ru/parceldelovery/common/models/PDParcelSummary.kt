package ru.parceldelovery.common.models

import java.time.OffsetDateTime

/**
 * Краткая информация о посылке для результатов поиска и отчётов
 */
data class PDParcelSummary(
    val trackNumber: String,
    /** Идентификатор клиента-отправителя */
    val senderId: String,
    /** Идентификатор клиента-получателя */
    val receiverId: String,
    val deliveryAddress: String,
    val status: PDStatus,
    val createdAt: OffsetDateTime? = null
)