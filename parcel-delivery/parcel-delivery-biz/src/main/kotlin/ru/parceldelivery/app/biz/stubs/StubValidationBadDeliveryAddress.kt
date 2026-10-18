package ru.parceldelivery.app.biz.stubs

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.stubs.ContextStubs

fun ICorChainDsl<PDContext>.stubValidationBadDeliveryAddress(title: String = "Имитация ошибки валидации адреса доставки") =
    worker(title) {
        if (stubCase == ContextStubs.VALIDATION_BAD_DELIVERY_ADDRESS) {
            errors.add(
                PDError(
                    code = "validation-delivery-address-empty",
                    field = "deliveryAddress",
                    message = "delivery address must not be blank",
                    level = LogLevel.ERROR
                )
            )
            state = PDState.FAILING
        }
    }
