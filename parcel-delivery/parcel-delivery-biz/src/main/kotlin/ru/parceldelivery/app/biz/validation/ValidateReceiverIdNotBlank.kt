package ru.parceldelivery.app.biz.validation

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDError

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
