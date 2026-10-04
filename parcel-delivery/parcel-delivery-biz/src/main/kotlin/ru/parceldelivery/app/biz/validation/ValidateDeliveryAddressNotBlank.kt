package ru.parceldelivery.app.biz.validation

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDError

fun ICorChainDsl<PDContext>.validateDeliveryAddressNotBlank(title: String = "Проверка адреса") = worker(title) {
    if (state == PDState.RUNNING && pdValidating.deliveryAddress.isBlank()) {
        errors.add(
            PDError(
                code = "validation-delivery-address-empty",
                field = "delivery-address",
                message = "delivery address must not be blank",
                level = LogLevel.ERROR
            )
        )
        state = PDState.FAILING
    }
}
