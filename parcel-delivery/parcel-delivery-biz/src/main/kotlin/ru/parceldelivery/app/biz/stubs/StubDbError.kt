package ru.parceldelivery.app.biz.stubs

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.stubs.ContextStubs

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
