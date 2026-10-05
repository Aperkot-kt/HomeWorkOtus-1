package ru.parceldelivery.common.repo

import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.models.PDParcel

sealed interface IDbPDResponse: IDbResponse<PDParcel>

data class DbPDResponseOk(
    val data: PDParcel
): IDbPDResponse

data class DbPDResponseErr(
    val errors: List<PDError> = emptyList()
): IDbPDResponse {
    constructor(err: PDError): this(listOf(err))
}

data class DbPDResponseErrWithData(
    val data: PDParcel,
    val errors: List<PDError> = emptyList()
): IDbPDResponse {
    constructor(pd: PDParcel, err: PDError): this(pd, listOf(err))
}
