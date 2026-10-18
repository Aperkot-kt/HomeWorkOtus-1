package ru.parcel.delivery.backend.repo.tests

import ru.parceldelivery.common.models.*

abstract class BaseInitPDs(private val op: String) : IInitObjects<PDParcel> {
    open val lockOld: PDLock = PDLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: PDLock = PDLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        senderId: PDUserId = PDUserId("sender-123"),
        receiverId: PDUserId = PDUserId("receiver-124"),
        status: PDStatus = PDStatus.ACCEPTED,
        lock: PDLock = lockOld,
    ) = PDParcel(
        trackNumber = PDParcelId("pd-repo-$op-$suf"),
        senderId = senderId,
        receiverId = receiverId,
        weight = 1.0,
        dimensions = PDParcelDimensions(40.0, 30.0, 20.0),
        status = status,
        deliveryAddress = "$suf delivery address",
        lock = lock,
    )
}
