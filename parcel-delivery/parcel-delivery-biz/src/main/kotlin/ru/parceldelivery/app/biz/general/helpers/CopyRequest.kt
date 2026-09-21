package ru.parceldelivery.app.biz.general.helpers

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker

fun ICorChainDsl<PDContext>.copyRequest() =
    worker("Копируем поля в pdValidating") { pdValidating = pdRequest.copy() }
