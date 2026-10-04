package ru.parcel.delivery.stubs

import ru.parceldelivery.common.models.PDLock
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelDimensions
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDParcelSummary
import ru.parceldelivery.common.models.PDFilter
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId
import java.time.OffsetDateTime

/**
 * Заглушки полной информации о посылке и её кратких карточек
 */
object PDParcelStub {

    val PARCEL1: PDParcel
        get() = PDParcel(
            trackNumber = PDParcelId("PD-2026-6660001"),
            senderId = PDUserId("user-1"),
            receiverId = PDUserId("user-2"),
            weight = 1.5,
            dimensions = PDParcelDimensions(length = 30.0, width = 20.0, height = 10.0),
            status = PDStatus.IN_TRANSIT,
            deliveryAddress = "г. Москва, ул. Тверская, д. 7, кв. 12",
            createdAt = OffsetDateTime.parse("2026-09-01T10:00:00+03:00"),
            updatedAt = OffsetDateTime.parse("2026-09-05T14:30:00+03:00"),
            lock = PDLock("123-234-abc-ABC"),
        )

    fun get(): PDParcel = PARCEL1.copy()

    fun prepareResult(block: (PDParcel) -> PDParcel): PDParcel = block(get())

    fun getSummary(): PDParcelSummary =
        PDParcelSummary(
            trackNumber = PARCEL1.trackNumber.asString(),
            senderId = PARCEL1.senderId.asString(),
            receiverId = PARCEL1.receiverId.asString(),
            deliveryAddress = PARCEL1.deliveryAddress,
            status = PARCEL1.status ?: PDStatus.NONE,
            createdAt = PARCEL1.createdAt,
        )

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
        getSummary().copy(
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
