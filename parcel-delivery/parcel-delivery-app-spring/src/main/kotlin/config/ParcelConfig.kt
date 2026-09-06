package ru.parceldelivery.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.parceldelivery.app.common.PDCorSettings
import ru.parceldelivery.app.common.PDParcelProcessor
import ru.parceldelivery.app.spring.base.PDAppSettings

/**
 * Бин-конфигурация Spring-приложения доставки посылок:
 * бизнес-процессор, инфраструктурные настройки и настройки приложения.
 */
@Configuration
class ParcelConfig {

    @Bean
    fun pdParcelProcessor(): PDParcelProcessor = PDParcelProcessor()

    @Bean
    fun pdCorSettings(): PDCorSettings = PDCorSettings()

    @Bean
    fun appSettings(
        corSettings: PDCorSettings,
        processor: PDParcelProcessor,
    ): PDAppSettings = PDAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}