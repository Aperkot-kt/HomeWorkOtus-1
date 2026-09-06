package ru.parceldelivery.app.common

import kotlinx.datetime.Clock
import ru.parceldelovery.common.PDContext
import ru.parceldelovery.common.models.LogLevel
import ru.parceldelovery.common.models.PDCommand
import ru.parceldelovery.common.models.PDError
import ru.parceldelovery.common.models.PDState
import kotlin.reflect.KClass

/**
 * Обёртка обработки запроса приложения доставки посылок:
 * логирование, запуск бизнес-процессора, аккумулирование ошибок и формирование ответа.
 */
suspend inline fun <T> IPDAppSettings.controllerHelper(
    crossinline getRequest: suspend PDContext.() -> Unit,
    crossinline toResponse: suspend PDContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.log
    val ctx = PDContext(timeStart = Clock.System.now())
    return try {
        ctx.getRequest()
        logger.info("Request $logId started for ${clazz.simpleName}: {}", ctx.toLog(logId))
        processor.exec(ctx)
        logger.info("Request $logId processed for ${clazz.simpleName}: {}", ctx.toLog(logId))
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error("Request $logId failed for ${clazz.simpleName}: {}", ctx.toLog(logId), e)
        ctx.state = PDState.FAILING
        ctx.errors.add(e.asPDError())
        processor.exec(ctx)
        if (ctx.command == PDCommand.NONE) {
            ctx.command = PDCommand.READ
        }
        ctx.toResponse()
    }
}

/**
 * Строковое представление контекста для логов
 */
fun PDContext.toLog(logId: String): String =
    "logId=$logId, command=$command, state=$state, errors=${errors.map { it.code }}"

/**
 * Преобразование исключения в ошибку контекста
 */
fun Throwable.asPDError(): PDError = PDError(
    code = "INTERNAL_ERROR",
    message = message ?: javaClass.simpleName,
    level = LogLevel.ERROR,
    exception = this,
)