package ru.parceldelovery.common

import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.models.PDRequestId
import ru.parceldelovery.common.models.PDState
import ru.parceldelovery.common.models.PDWorkMode
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDParcel
import ru.parceldelovery.common.stubs.ContextStubs
import kotlinx.datetime.Instant
import ru.parceldelovery.common.models.PDFilter


data class PDContext(
    var command: PDCommand = PDCommand.NONE,
    var state: PDState = PDState.NONE,
    val errors: MutableList<PDError> = mutableListOf(),

    var workMode: PDWorkMode = PDWorkMode.PROD,
    var stubCase: ContextStubs = ContextStubs.NONE,

    var requestId: PDRequestId = PDRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var pdRequest: PDParcel = PDParcel(),
    var pdFilterRequest: PDFilter = PDFilter(),

    //для одиночного ответа
    var pdResponse: PDParcel = PDParcel(),
    //для ответа-списка
    var pdsResponse: MutableList<PDParcel> = mutableListOf(),
)
