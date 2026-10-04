package ru.parceldelivery.app.biz.stubs

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.stubs.ContextStubs
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId

fun ICorChainDsl<PDContext>.stubUpdateSuccess(title: String = "Имитация успешного обновления") = worker(title) {
    if (stubCase == ContextStubs.SUCCESS) {
        parcelRepoDone = PDParcel(
            trackNumber = PDParcelId("PD-2026-000001"),
            senderId = PDUserId("CL-1001"),
            receiverId = PDUserId("CL-1002"),
            weight = 5.5,
            status = PDStatus.DELIVERED,
            deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 10"
        )
        state = PDState.FINISHING
    }
}
