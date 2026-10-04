package ru.parceldelivery.app.biz.general

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.PDState

fun ICorChainDsl<PDContext>.initStatus(title: String = "Инициализация статуса") = worker(title) {
    state = PDState.RUNNING
    errors = mutableListOf()
}
