package ru.parceldelivery.app.biz.repo

import ru.parceldelivery.api.log.v1.logInfo
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.PDState

fun ICorChainDsl<PDContext>.repoUpdate() = worker("Обновляем посылку в репозитории") {
    if (state == PDState.FINISHING) {
        logInfo("REPO: updating parcel $pdValidating")
        parcelRepoDone = pdValidating.copy()
    }
}
