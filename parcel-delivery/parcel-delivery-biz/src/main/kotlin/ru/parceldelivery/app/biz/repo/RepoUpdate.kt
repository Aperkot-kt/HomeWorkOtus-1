package ru.parceldelivery.app.biz.repo

import ru.parceldelivery.api.log.v1.logInfo
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.repo.DbPDRequest
import ru.parceldelivery.common.repo.DbPDResponseErr
import ru.parceldelivery.common.repo.DbPDResponseErrWithData
import ru.parceldelivery.common.repo.DbPDResponseOk
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.PDState

fun ICorChainDsl<PDContext>.repoUpdate() = worker("Обновляем посылку в репозитории") {
    if (state == PDState.FINISHING) {
        logInfo("REPO: updating parce/l $pdValidating")
        when (val result = corSettings.repo.updatePD(DbPDRequest(pdValidating.copy()))) {
            is DbPDResponseOk -> parcelRepoDone = result.data
            is DbPDResponseErr -> {
                errors.addAll(result.errors)
                state = PDState.FAILING
            }
            is DbPDResponseErrWithData -> {
                errors.addAll(result.errors)
                state = PDState.FAILING
            }
        }
    }
}
