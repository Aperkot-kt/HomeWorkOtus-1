package ru.parceldelivery.app.biz.validation

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDError

fun ICorChainDsl<PDContext>.validateWeightPositive(title: String = "Проверка веса посылки") = worker(title) {
    if (state == PDState.RUNNING && pdValidating.weight <= 0.0) {
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
