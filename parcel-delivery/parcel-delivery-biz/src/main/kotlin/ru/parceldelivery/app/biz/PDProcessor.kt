package ru.parceldelivery.app.biz

import ru.parceldelivery.app.biz.general.initStatus
import ru.parceldelivery.app.biz.operations.parcelCreate
import ru.parceldelivery.app.biz.operations.parcelDelete
import ru.parceldelivery.app.biz.operations.parcelRead
import ru.parceldelivery.app.biz.operations.parcelSearch
import ru.parceldelivery.app.biz.operations.parcelUpdate
import ru.parceldelivery.app.common.IPDProcessor
import ru.parceldelivery.common.PDCorSettings
import ru.parceldelivery.app.lib.rootChain
import ru.parceldelivery.common.PDContext

class PDProcessor(
    private val corSettings: PDCorSettings = PDCorSettings.NONE,
) : IPDProcessor {

    override suspend fun exec(ctx: PDContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain {
        initStatus()
        parcelCreate()
        parcelRead()
        parcelUpdate()
        parcelDelete()
        parcelSearch()
    }.build()
}