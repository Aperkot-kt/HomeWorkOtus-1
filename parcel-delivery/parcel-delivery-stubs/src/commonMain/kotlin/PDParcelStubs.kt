package ru.parcel.delivery.stubs

import ru.parceldelovery.common.models.PDLock
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelDimensions
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDStatus
import ru.parceldelovery.common.models.PDUserId
import java.time.OffsetDateTime

/**
 * Заглушка полной информации о посылке
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

    fun prepareResult(block: PDParcel.() -> Unit): PDParcel = get().apply(block)
}