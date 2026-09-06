package ru.parceldelivery.app.kafka

import ru.parceldelivery.app.common.IPDAppSettings
import ru.parceldelivery.app.common.PDCorSettings
import ru.parceldelivery.app.common.PDParcelProcessor

/**
 * Конфигурация Kafka-приложения.
 *
 * Значения читаются из переменных окружения;
 * значения по умолчанию предназначены для локальной разработки.
 */
class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicNotification: String = KAFKA_TOPIC_NOTIFICATION,
    override val corSettings: PDCorSettings = PDCorSettings(),
    override val processor: PDParcelProcessor = PDParcelProcessor(),
): IPDAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"
        const val KAFKA_TOPIC_NOTIFICATION_VAR = "KAFKA_TOPIC_NOTIFICATION"

        val KAFKA_HOSTS by lazy {
            (System.getenv(KAFKA_HOST_VAR) ?: "localhost:9092").split("\\s*[,; ]\\s*").filter(String::isNotEmpty)
        }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "parcel-delivery" }
        val KAFKA_TOPIC_NOTIFICATION by lazy { System.getenv(KAFKA_TOPIC_NOTIFICATION_VAR) ?: "notification" }
    }
}