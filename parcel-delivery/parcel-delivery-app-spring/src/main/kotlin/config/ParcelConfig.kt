package ru.parceldelivery.app.spring.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.yan.swiftorder.appspring.config.RepoConfig
import ru.parcel.delivery.backend.repo.postgresql.RepoPDSql
import ru.parcel.delivery.backend.repo.postgresql.SqlProperties
import ru.parcel.delivery.repo.inmemory.PDRepoInMemory
import ru.parceldelivery.app.biz.PDProcessor
import ru.parceldelivery.common.PDCorSettings
import ru.parceldelivery.app.common.PDParcelProcessor
import ru.parceldelivery.app.spring.base.PDAppSettings
import ru.parceldelivery.common.repo.IRepoPD
import ru.parceldelivery.common.repo.PDRepoBase

/**
 * Бин-конфигурация Spring-приложения доставки посылок:
 * бизнес-процессор, инфраструктурные настройки и настройки приложения.
 */
@Configuration
@EnableConfigurationProperties(RepoConfig::class)
class ParcelConfig(private val repoConfig: RepoConfig) {

    @Bean
    fun pdParcelProcessor(): PDProcessor {

        val repo: IRepoPD = when (repoConfig.type) {
            RepoConfig.RepoType.INMEMORY -> PDRepoInMemory()
            RepoConfig.RepoType.POSTGRES -> RepoPDSql(
                properties = SqlProperties(
                    host = repoConfig.config.host,
                    port = repoConfig.config.port,
                    user = repoConfig.config.user,
                    password = repoConfig.config.password,
                    database = repoConfig.config.database,
                )
            )
        }
        return PDProcessor(corSettings = PDCorSettings(repo = repo))
    }

    @Bean
    fun pdCorSettings(): PDCorSettings = PDCorSettings()

    @Bean
    fun appSettings(
        corSettings: PDCorSettings,
        processor: PDProcessor,
    ): PDAppSettings = PDAppSettings(
        corSettings = corSettings,
        processor = processor,
        )
}