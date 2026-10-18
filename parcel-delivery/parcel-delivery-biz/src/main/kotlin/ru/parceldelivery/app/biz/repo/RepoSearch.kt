package ru.parceldelivery.app.biz.repo

import ru.parceldelivery.api.log.v1.logInfo
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.repo.DbPDFilterRequest
import ru.parceldelivery.common.repo.DbPDsResponseErr
import ru.parceldelivery.common.repo.DbPDsResponseOk
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.worker
import ru.parceldelivery.common.models.PDState
import ru.parceldelivery.common.models.PDStatus
import ru.parceldelivery.common.models.PDUserId

fun ICorChainDsl<PDContext>.repoSearch() = worker("Ищем посылки в репозитории") {
    if (state == PDState.FINISHING) {
        logInfo("REPO: searching parcels")
        val request = DbPDFilterRequest(
            status = pdFilterRequest.status ?: PDStatus.NONE,
            senderId = pdFilterRequest.senderId?.let { PDUserId(it) } ?: PDUserId.NONE,
            receiverId = pdFilterRequest.receiverId?.let { PDUserId(it) } ?: PDUserId.NONE,
        )
        when (val result = corSettings.repo.searchPD(request)) {
            is DbPDsResponseOk  -> parcelsRepoDone = result.data.toMutableList()
            is DbPDsResponseErr -> { errors.addAll(result.errors); state = PDState.FAILING }
        }
    }
}
