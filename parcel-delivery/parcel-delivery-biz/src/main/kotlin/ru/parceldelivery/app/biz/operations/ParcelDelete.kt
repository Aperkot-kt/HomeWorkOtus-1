package ru.parceldelivery.app.biz.operations

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.biz.general.helpers.copyRequest
import ru.parceldelivery.app.biz.general.helpers.finishValidation
import ru.parceldelivery.app.biz.general.operation
import ru.parceldelivery.app.biz.general.stubs
import ru.parceldelivery.app.biz.repo.repoDelete
import ru.parceldelivery.app.biz.stubs.stubDbError
import ru.parceldelivery.app.biz.stubs.stubDeleteSuccess
import ru.parceldelivery.app.biz.stubs.stubNoCase
import ru.parceldelivery.app.biz.stubs.stubValidationBadId
import ru.parceldelivery.app.biz.validation.validateIdNotBlank
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.chain
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDWorkMode

fun ICorChainDsl<PDContext>.parcelDelete() = operation(PDCommand.DELETE) {
    stubs {
        stubDeleteSuccess()
        stubValidationBadId()
        stubDbError()
        stubNoCase()
    }
    chain {
        on { workMode != PDWorkMode.STUB && state == PDState.RUNNING }
        copyRequest()
        validateIdNotBlank()
        finishValidation()
        repoDelete()
    }
}
