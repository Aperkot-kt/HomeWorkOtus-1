package ru.parceldelivery.app.biz.stubs

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.stubs.ContextStubs

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
