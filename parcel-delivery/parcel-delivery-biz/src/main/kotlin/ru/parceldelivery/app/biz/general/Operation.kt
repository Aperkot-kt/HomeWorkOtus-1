package ru.parceldelivery.app.biz.general

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.chain
import ru.parceldelivery.common.models.PDCommand

fun ICorChainDsl<PDContext>.operation(
    command: PDCommand,
    block: ICorChainDsl<PDContext>.() -> Unit,
) = chain {
    on { this.command == command }
    block()
}
