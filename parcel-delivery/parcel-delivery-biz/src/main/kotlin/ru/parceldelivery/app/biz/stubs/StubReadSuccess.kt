package ru.parceldelivery.app.biz.stubs

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.stubs.ContextStubs
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.models.PDParcelId
import ru.parceldelovery.common.models.PDStatus
import ru.parceldelovery.common.models.PDUserId

fun ICorChainDsl<PDContext>.stubReadSuccess(title: String = "Имитация успешного чтения") = worker(title) {
    if (stubCase == ContextStubs.SUCCESS) {
        parcelRepoDone = PDParcel(
            trackNumber = PDParcelId("PD-2026-000001"),
            senderId = PDUserId("CL-1001"),
            receiverId = PDUserId("CL-1002"),
            weight = 5.5,
            status = PDStatus.IN_TRANSIT,
            deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 10"
        )
        state = PDState.FINISHING
    }
}
