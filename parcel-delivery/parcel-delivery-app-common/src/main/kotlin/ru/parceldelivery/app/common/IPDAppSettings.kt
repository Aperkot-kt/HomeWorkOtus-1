package ru.parceldelivery.app.common

/**
 * Абстракция настроек приложения сервиса доставки посылок:
 * бизнес-процессор и инфраструктурные настройки.
 */
interface IPDAppSettings {

    /** Процессор бизнес-операций с посылками */
    val processor: PDParcelProcessor

    /** Инфраструктурные настройки приложения */
    val corSettings: PDCorSettings
}