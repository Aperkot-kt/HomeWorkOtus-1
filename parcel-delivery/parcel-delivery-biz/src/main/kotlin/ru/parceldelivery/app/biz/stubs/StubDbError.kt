package ru.parceldelivery.app.biz.stubs

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.stubs.ContextStubs

fun ICorChainDsl<PDContext>.stubDbError(title: String = "Имитация ошибки работы с БД") = worker(title) {
    if (stubCase == ContextStubs.DB_ERROR) {
        errors.add(
            PDError(
                code = "db-error",
                group = "database",
                message = "database error occurred",
                level = LogLevel.ERROR
            )
        )
        state = PDState.FAILING
    }
}
