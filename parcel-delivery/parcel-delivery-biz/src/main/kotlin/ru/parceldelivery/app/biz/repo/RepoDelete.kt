package ru.parceldelivery.app.biz.repo

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.PDState

fun ICorChainDsl<PDContext>.repoDelete() = worker("Удаляем посылку из репозитория") {
    if (state == PDState.FINISHING) {
        println("REPO: deleting parcel by id ${pdValidating.trackNumber}")
    }
}
