package ru.parceldelivery.app.biz.operations

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.biz.general.helpers.copyRequest
import ru.parceldelivery.app.biz.general.helpers.finishValidation
import ru.parceldelivery.app.biz.general.operation
import ru.parceldelivery.app.biz.general.stubs
import ru.parceldelivery.app.biz.general.helpers.trimDeliveryAddress
import ru.parceldelivery.app.biz.repo.repoUpdate
import ru.parceldelivery.app.biz.stubs.stubDbError
import ru.parceldelivery.app.biz.stubs.stubNoCase
import ru.parceldelivery.app.biz.stubs.stubUpdateSuccess
import ru.parceldelivery.app.biz.stubs.stubValidationBadWeight
import ru.parceldelivery.app.biz.stubs.stubValidationBadId
import ru.parceldelivery.app.biz.validation.validateWeightPositive
import ru.parceldelivery.app.biz.validation.validateIdNotBlank
import ru.parceldelivery.app.biz.validation.validateSenderIdNotBlank
import ru.parceldelivery.app.biz.validation.validateReceiverIdNotBlank
import ru.parceldelivery.app.lib.ICorChainDsl
import ru.parceldelivery.app.lib.chain
import ru.parceldelovery.common.models.PDState
import ru.parceldelivery.app.biz.stubs.stubValidationBadDeliveryAddress
import ru.parceldelivery.app.biz.stubs.stubValidationBadReceiverId
import ru.parceldelivery.app.biz.stubs.stubValidationBadSenderId
import ru.parceldelivery.app.biz.validation.validateDeliveryAddressNotBlank
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDWorkMode

fun ICorChainDsl<PDContext>.parcelUpdate() = operation(PDCommand.UPDATE) {
    stubs {
        stubUpdateSuccess()
        stubValidationBadId()
        stubValidationBadWeight()
        stubValidationBadDeliveryAddress()
        stubValidationBadSenderId()
        stubValidationBadReceiverId()
        stubDbError()
        stubNoCase()
    }
    chain {
        on { workMode != PDWorkMode.STUB && state == PDState.RUNNING }
        copyRequest()
        trimDeliveryAddress()
        validateIdNotBlank()
        validateWeightPositive()
        validateDeliveryAddressNotBlank()
        validateSenderIdNotBlank()
        validateReceiverIdNotBlank()
        finishValidation()
        repoUpdate()
    }
}
