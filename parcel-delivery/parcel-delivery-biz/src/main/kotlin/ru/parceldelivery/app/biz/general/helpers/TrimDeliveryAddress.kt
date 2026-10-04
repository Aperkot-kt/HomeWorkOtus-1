package ru.parceldelivery.app.biz.general.helpers

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker

fun ICorChainDsl<PDContext>.trimDeliveryAddress() =
    worker("Очистка delivery address") {
        pdValidating = pdValidating.copy(deliveryAddress = pdValidating.deliveryAddress.trim())
    }
