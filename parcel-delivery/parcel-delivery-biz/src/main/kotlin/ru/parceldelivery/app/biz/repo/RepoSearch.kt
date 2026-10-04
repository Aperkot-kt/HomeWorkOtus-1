package ru.parceldelivery.app.biz.repo

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.PDState

fun ICorChainDsl<PDContext>.repoSearch() = worker("Ищем посылки в репозитории") {
    if (state == PDState.FINISHING) {
        println("REPO: searching parcels")
        parcelsRepoDone = mutableListOf()
    }
}
