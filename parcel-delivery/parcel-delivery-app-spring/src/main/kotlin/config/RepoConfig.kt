package ru.otus.otuskotlin.yan.swiftorder.appspring.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "parcel-delivery.repo")
data class RepoConfig(
    val type: RepoType = RepoType.POSTGRES,
    val config: RepoSpecificConfig = RepoSpecificConfig(),
) {
    enum class RepoType { INMEMORY, POSTGRES }

    data class RepoSpecificConfig(
        val host: String = "localhost",
        val port: Int = 5432,
        val user: String = "postgres",
        val password: String = "pd-pass",
        val database: String = "pd_pds",
    )
}
