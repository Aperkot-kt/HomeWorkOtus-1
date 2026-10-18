package ru.parceldelivery.app.biz.validation

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.models.LogLevel

fun ICorChainDsl<PDContext>.validateIdNotBlank(title: String = "Проверка идентификтаора посылки") = worker(title) {
    if (state == PDState.RUNNING && pdValidating.trackNumber.asString().isBlank()) {
        errors.add(
            PDError(
                code = "validation-id-empty",
                field = "id",
                message = "id must not be blank",
                level = LogLevel.ERROR
            )
        )
        state = PDState.FAILING
    }
}
