package ru.parceldelivery.app.biz.validation

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDError

fun ICorChainDsl<PDContext>.validateSenderIdNotBlank(title: String = "Проверка идентификатора отправителя") =
    worker(title) {
        if (state == PDState.RUNNING && pdValidating.senderId.asString().isBlank()) {
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
