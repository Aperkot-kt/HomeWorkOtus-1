package ru.parcel.delivery.stubs

import ru.parceldelovery.common.models.PDFilter
import ru.parceldelovery.common.models.PDParcelSummary
import ru.parceldelovery.common.models.PDStatus

/**
 * Заглушка кратких карточек посылок
 */
object PDAdStub {

    fun get(): PDParcelSummary =
        PDParcelSummary(
            trackNumber = PDParcelStub.PARCEL1.trackNumber.asString(),
            senderId = PDParcelStub.PARCEL1.senderId.asString(),
            receiverId = PDParcelStub.PARCEL1.receiverId.asString(),
            deliveryAddress = PDParcelStub.PARCEL1.deliveryAddress,
            status = PDParcelStub.PARCEL1.status ?: PDStatus.NONE,
            createdAt = PDParcelStub.PARCEL1.createdAt,
        )

    fun prepareResult(block: PDParcelSummary.() -> Unit): PDParcelSummary = get().apply(block)

    fun prepareSearchList(filter: PDFilter = PDFilter()): List<PDParcelSummary> =
        listOf(
            summary("PD-2026-6660001", "user-1", "user-2", PDStatus.IN_TRANSIT),
            summary("PD-2026-6660002", "user-1", "user-3", PDStatus.ACCEPTED),
            summary("PD-2026-6660003", "user-2", "user-1", PDStatus.DELIVERED),
            summary("PD-2026-6660004", "user-2", "user-4", PDStatus.PENDING_DELIVERY),
            summary("PD-2026-6660005", "user-3", "user-1", PDStatus.IN_TRANSIT),
            summary("PD-2026-6660006", "user-3", "user-2", PDStatus.ACCEPTED),
        ).filter { it.matches(filter) }

    private fun summary(trackNumber: String, senderId: String, receiverId: String, status: PDStatus) =
        get().copy(
            trackNumber = trackNumber,
            senderId = senderId,
            receiverId = receiverId,
            status = status,
        )

    private fun PDParcelSummary.matches(filter: PDFilter): Boolean =
        (filter.status == null || status == filter.status) &&
            (filter.senderId == null || senderId == filter.senderId) &&
            (filter.receiverId == null || receiverId == filter.receiverId)
}