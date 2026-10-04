package ru.parceldelivery.common.repo

import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDError

sealed interface IDbPDsResponse: IDbResponse<List<PDParcel>>

data class DbPDsResponseOk(
    val data: List<PDParcel>
): IDbPDsResponse

@Suppress("unused")
data class DbPDsResponseErr(
    val errors: List<PDError> = emptyList()
): IDbPDsResponse {
    constructor(err: PDError): this(listOf(err))
}
