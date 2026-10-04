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

fun ICorChainDsl<PDContext>.stubSearchSuccess(title: String = "Имитация успешного поиска") = worker(title) {
    if (stubCase == ContextStubs.SUCCESS) {
        parcelsRepoDone = mutableListOf(
            PDParcel(
                trackNumber = PDParcelId("PD-2026-000001"),
                senderId = PDUserId("CL-1001"),
                receiverId = PDUserId("CL-1002"),
                weight = 5.5,
                status = PDStatus.IN_TRANSIT,
                deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 10"
            ),
            PDParcel(
                trackNumber = PDParcelId("PD-2026-000002"),
                senderId = PDUserId("CL-1003"),
                receiverId = PDUserId("CL-1004"),
                weight = 3.0,
                status = PDStatus.IN_TRANSIT,
                deliveryAddress = "г. Санкт-Петербург, ул. Невский пр., д. 20"
            ),
        )
        state = PDState.FINISHING
    }
}
