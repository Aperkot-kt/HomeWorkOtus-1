package ru.parceldelivery.api.log.v1

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.slf4j.LoggerFactory
import ru.parceldelivery.common.PDContext
import ru.parceldelivery.common.models.LogLevel
import ru.parceldelivery.common.models.PDError
import ru.parceldelivery.common.models.PDParcel
import ru.parceldelivery.common.models.PDParcelId
import ru.parceldelivery.common.models.PDRequestId
import ru.parceldelivery.common.models.PDUserId
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PDLoggerTest {

    private val slf4jLogger = LoggerFactory.getLogger("parcel-delivery-app") as Logger
    private val appender = ListAppender<ILoggingEvent>()

    @BeforeTest
    fun setUp() {
        appender.start()
        slf4jLogger.addAppender(appender)
    }

    @AfterTest
    fun tearDown() {
        slf4jLogger.detachAppender(appender)
        appender.stop()
        appender.list.clear()
    }

    @Test
    fun `info logs message with json snapshot of context`() {
        val ctx = PDContext().apply {
            requestId = PDRequestId("req-1")
            pdRequest = PDParcel(
                trackNumber = PDParcelId("PD-2026-000001"),
                senderId = PDUserId("CL-1001"),
            )
        }

        PDLogger.info(ctx, "REPO: reading parcel", "log-1")

        val event = appender.list.single()
        assertEquals(Level.INFO, event.level)
        assertTrue(event.formattedMessage.startsWith("REPO: reading parcel"))
        assertTrue(event.formattedMessage.contains("\"logId\":\"log-1\""))
        assertTrue(event.formattedMessage.contains("\"source\":\"parcel-delivery-app\""))
        assertTrue(event.formattedMessage.contains("\"requestId\":\"req-1\""))
        assertTrue(event.formattedMessage.contains("\"trackNumber\":\"PD-2026-000001\""))
        assertTrue(event.formattedMessage.contains("\"senderId\":\"CL-1001\""))
    }

    @Test
    fun `log id is generated and unique when not specified`() {
        PDLogger.info(PDContext(), "message 1")
        PDLogger.info(PDContext(), "message 2")

        val (first, second) = appender.list
        val firstId = first.formattedMessage.substringAfter("\"logId\":\"").substringBefore("\"")
        val secondId = second.formattedMessage.substringAfter("\"logId\":\"").substringBefore("\"")

        assertTrue(firstId.startsWith("log-"))
        assertTrue(secondId.startsWith("log-"))
        assertTrue(firstId != secondId)
    }

    @Test
    fun `error logs exception at error level`() {
        PDLogger.error(PDContext(), "REPO: boom", IllegalStateException("db down"), "log-9")

        val event = appender.list.single()
        assertEquals(Level.ERROR, event.level)
        assertTrue(event.formattedMessage.startsWith("REPO: boom"))
        assertEquals("db down", event.throwableProxy?.message)
    }

    @Test
    fun `worker extension logs with context as receiver`() {
        val ctx = PDContext().apply {
            errors.add(
                PDError(
                    code = "NOT_FOUND",
                    field = "trackNumber",
                    message = "not found",
                    level = LogLevel.ERROR,
                )
            )
        }

        ctx.logInfo("REPO: searching parcels")

        val event = appender.list.single()
        assertEquals(Level.INFO, event.level)
        assertTrue(event.formattedMessage.startsWith("REPO: searching parcels"))
        assertTrue(event.formattedMessage.contains("\"code\":\"NOT_FOUND\""))
    }
}
