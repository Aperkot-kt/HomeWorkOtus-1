package ru.parceldelivery.app.biz.stubs

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.stubs.ContextStubs

fun ICorChainDsl<PDContext>.stubValidationBadWeight(title: String = "Имитация ошибки валидации веса посылки") =
    worker(title) {
        if (stubCase == ContextStubs.VALIDATION_BAD_WEIGHT) {
            errors.add(
                PDError(
                    code = "validation-weight-not-positive",
                    field = "weight",
                    message = "weight must be positive",
                    level = LogLevel.ERROR
                )
            )
            state = PDState.FAILING
        }
    }
