package ru.parceldelivery.common.helpers

import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.models.PDState

fun Throwable.asPDError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = PDError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun PDContext.addError(error: PDError) = errors.add(error)
inline fun PDContext.addErrors(error: Collection<PDError>) = errors.addAll(error)

inline fun PDContext.fail(error: PDError) {
    addError(error)
    state = PDState.FAILING
}

inline fun PDContext.fail(errors: Collection<PDError>) {
    addErrors(errors)
    state = PDState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = PDError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = PDError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
