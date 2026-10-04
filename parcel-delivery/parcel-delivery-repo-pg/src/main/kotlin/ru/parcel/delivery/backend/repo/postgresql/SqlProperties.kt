package ru.parcel.delivery.backend.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "pd-pass",
    val database: String = "pd_pds",
    val schema: String = "public",
    val table: String = "pds",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
