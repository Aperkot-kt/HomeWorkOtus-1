package ru.parceldelivery.app.biz.general

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.chain
import ru.parceldelivery.common.models.PDWorkMode

fun ICorChainDsl<PDContext>.stubs(block: ICorChainDsl<PDContext>.() -> Unit) = chain {
    on { workMode == PDWorkMode.STUB }
    block()
}
