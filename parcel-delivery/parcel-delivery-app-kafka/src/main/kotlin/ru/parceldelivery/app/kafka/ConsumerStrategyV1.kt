package ru.parceldelivery.app.kafka

import ru.parceldelivery.api.v1.models.IRequest
import ru.parceldelivery.api.v1.models.IResponse
import ru.parceldelivery.mappers.apiV1RequestDeserialize
import ru.parceldelivery.mappers.apiV1ResponseSerialize
import ru.parceldelivery.mappers.fromTransport
import ru.parceldelivery.mappers.toTransport
import ru.parceldelovery.common.PDContext
import ru.parceldelivery.app.kafka.AppKafkaConfig

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicNotification, config.kafkaTopicNotification)
    }

    override fun serialize(source: PDContext): String {
        val response: IResponse = source.toTransport()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: PDContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}