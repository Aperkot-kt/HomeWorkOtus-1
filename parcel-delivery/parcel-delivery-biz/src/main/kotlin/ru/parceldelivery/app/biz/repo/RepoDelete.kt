package ru.parceldelivery.app.biz.repo

import ru.parceldelivery.api.log.v1.logInfo
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.repo.DbPDIdRequest
import ru.parceldelivery.common.repo.DbPDResponseErr
import ru.parceldelivery.common.repo.DbPDResponseErrWithData
import ru.parceldelivery.common.repo.DbPDResponseOk
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.PDState

fun ICorChainDsl<PDContext>.repoDelete() =  worker("Удаляем посылку из репозитория") {
    if (state == PDState.FINISHING) {
        logInfo("REPO: deleting parcel by id ${pdValidating.trackNumber}")
        when (val result = corSettings.repo.deletePD(DbPDIdRequest(pdValidating.copy().trackNumber))) {
            is DbPDResponseOk  -> parcelRepoDone = result.data
            is DbPDResponseErr -> { errors.addAll(result.errors); state = PDState.FAILING }
            is DbPDResponseErrWithData -> { errors.addAll(result.errors); state = PDState.FAILING }
        }
    }
}
