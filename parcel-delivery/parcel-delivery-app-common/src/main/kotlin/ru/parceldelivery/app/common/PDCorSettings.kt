package ru.parceldelivery.app.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration

/**
 * Инфраструктурные настройки приложения сервиса доставки посылок.
 */
data class PDCorSettings(
    /** Логгер приложения */
    val log: Logger = LoggerFactory.getLogger("parcel-delivery-app"),
    /** Таймаут обработки запроса */
    val timeout: Duration = Duration.ofSeconds(10),
)