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
import ru.parceldelivery.common.models.PDStatus
import java.time.OffsetDateTime

fun ICorChainDsl<PDContext>.repoCreate() = worker("Создаём посылку в репозитории") {
    if (state == PDState.FINISHING) {
        logInfo("REPO: creating parcel $pdValidating")
        val now = OffsetDateTime.now()
        val pdToCreate = pdValidating.copy(
            status = pdValidating.status ?: PDStatus.ACCEPTED,
            createdAt = pdValidating.createdAt ?: now,
            updatedAt = pdValidating.updatedAt ?: now,
        )
        when (val result = corSettings.repo.createPD(DbPDRequest(pdToCreate))) {
            is DbPDResponseOk  -> parcelRepoDone = result.data
            is DbPDResponseErr -> { errors.addAll(result.errors); state = PDState.FAILING }
            is DbPDResponseErrWithData -> { errors.addAll(result.errors); state = PDState.FAILING }
        }
    }
}
