package ru.parceldelivery.app.biz.general.helpers

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.PDState

fun ICorChainDsl<PDContext>.finishValidation(title: String = "Завершение валидации") = worker(title) {
    if (state == PDState.RUNNING && errors.isEmpty()) {
        state = PDState.FINISHING
    }
}
