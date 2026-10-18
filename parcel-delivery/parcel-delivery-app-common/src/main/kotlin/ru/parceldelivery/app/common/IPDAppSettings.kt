package ru.parceldelivery.app.common

import ru.parceldelivery.api.log.v1.PDLoggerProvider
import ru.parceldelivery.common.PDCorSettings

/**
 * Абстракция настроек приложения сервиса доставки посылок:
 * бизнес-процессор и инфраструктурные настройки.
 */
interface IPDAppSettings {

    /** Процессор бизнес-операций с посылками */
    val processor: IPDProcessor

    /** Инфраструктурные настройки приложения */
    val corSettings: PDCorSettings

    /** Провайдер логгера приложения */
    val loggerProvider: PDLoggerProvider
}