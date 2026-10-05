package ru.parceldelivery.app.biz.repo

import ru.parceldelivery.api.log.v1.logInfo
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.PDState

fun ICorChainDsl<PDContext>.repoRead() = worker("Читаем посылку из репозитория") {
    if (state == PDState.FINISHING) {
        logInfo("REPO: reading parcel by id ${pdValidating.trackNumber}")
        parcelRepoDone = pdValidating.copy()
    }
}
