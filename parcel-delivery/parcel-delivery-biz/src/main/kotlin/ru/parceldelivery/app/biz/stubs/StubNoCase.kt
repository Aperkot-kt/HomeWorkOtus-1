package ru.parceldelivery.app.biz.stubs

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.models.PDState

fun ICorChainDsl<PDContext>.stubNoCase(title: String = "Ошибка: запрошенный стаб недопустим") = worker(title) {
    if (state == PDState.RUNNING) {
        errors.add(
            PDError(
                code = "stub-not-found",
                message = "stub case '$stubCase' is not supported",
                level = LogLevel.ERROR
            )
        )
        state = PDState.FAILING
    }
}
