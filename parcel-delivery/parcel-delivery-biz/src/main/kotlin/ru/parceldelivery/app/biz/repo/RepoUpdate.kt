package ru.parceldelivery.app.biz.repo

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelovery.common.models.PDState

fun ICorChainDsl<PDContext>.repoUpdate() = worker("Обновляем посылку в репозитории") {
    if (state == PDState.FINISHING) {
        println("REPO: updating parcel $pdValidating")
        parcelRepoDone = pdValidating.copy()
    }
}
