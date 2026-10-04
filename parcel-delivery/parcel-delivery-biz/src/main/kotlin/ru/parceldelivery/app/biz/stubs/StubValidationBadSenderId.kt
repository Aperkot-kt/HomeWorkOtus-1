package ru.parceldelivery.app.biz.stubs

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.stubs.ContextStubs

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
