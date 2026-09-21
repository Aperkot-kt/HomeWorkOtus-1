package ru.parceldelivery.app.biz.stubs

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.stubs.ContextStubs

fun ICorChainDsl<PDContext>.stubValidationBadSenderId(title: String = "Имитация ошибки валидации senderId") =
    worker(title) {
        if (stubCase == ContextStubs.VALIDATION_BAD_SENDER_ID) {
            errors.add(
                PDError(
                    code = "validation-sender-id-empty",
                    field = "senderId",
                    message = "senderId must not be blank",
                    level = LogLevel.ERROR
                )
            )
            state = PDState.FAILING
        }
    }
