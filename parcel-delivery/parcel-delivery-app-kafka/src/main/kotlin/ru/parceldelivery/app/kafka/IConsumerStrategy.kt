package ru.parceldelivery.app.kafka

import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.kafka.AppKafkaConfig
/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: PDContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: PDContext)
}