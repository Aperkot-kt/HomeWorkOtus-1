package ru.parceldelivery.api.log.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory
import ru.parceldelivery.api.log.v1.mapper.toLog
import ru.parceldelivery.common.PDContext
import java.util.UUID

object PDLogger {

    private val logger = LoggerFactory.getLogger("parcel-delivery-app")
    private val mapper = ObjectMapper().registerKotlinModule()

    fun info(ctx: PDContext, message: String, logId: String = newLogId()) {
        logger.info("{} {}", message, json(ctx, logId))
    }

    fun error(ctx: PDContext, message: String, e: Throwable? = null, logId: String = newLogId()) {
        if (e != null) {
            logger.error("{} {}", message, json(ctx, logId), e)
        } else {
            logger.error("{} {}", message, json(ctx, logId))
        }
    }

    fun newLogId(): String = "log-${UUID.randomUUID()}"

    private fun json(ctx: PDContext, logId: String): String =
        mapper.writeValueAsString(ctx.toLog(logId))
}

fun PDContext.logInfo(message: String, logId: String = PDLogger.newLogId()) {
    PDLogger.info(this, message, logId)
}

fun PDContext.logError(message: String, e: Throwable? = null, logId: String = PDLogger.newLogId()) {
    PDLogger.error(this, message, e, logId)
}
