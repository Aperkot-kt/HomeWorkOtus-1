package ru.parceldelivery.app.biz.general

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.chain
import ru.parceldelovery.common.models.PDWorkMode

fun ICorChainDsl<PDContext>.stubs(block: ICorChainDsl<PDContext>.() -> Unit) = chain {
    on { workMode == PDWorkMode.STUB }
    block()
}
