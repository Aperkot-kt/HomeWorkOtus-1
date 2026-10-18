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

/**
 * Ошибка с актуальным состоянием объекта (`data`): приходит, например,
 * при конкурентном изменении, чтобы бизнес-логика видела настоящее значение.
 */
data class DbPDResponseErrWithData(
    val data: PDParcel,
    val errors: List<PDError> = emptyList()
): IDbPDResponse {
    constructor(data: PDParcel, err: PDError): this(data, listOf(err))
}