package ru.parceldelivery.app.biz.validation

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDError

fun ICorChainDsl<PDContext>.validateReceiverIdNotBlank(title: String = "Проверка идентификтора получателя") =
    worker(title) {
        if (state == PDState.RUNNING && pdValidating.receiverId.asString().isBlank()) {
            errors.add(
                PDError(
                    code = "validation-receiver-id-empty",
                    field = "receiverId",
                    message = "receiverId must not be blank",
                    level = LogLevel.ERROR
                )
            )
            state = PDState.FAILING
        }
    }
