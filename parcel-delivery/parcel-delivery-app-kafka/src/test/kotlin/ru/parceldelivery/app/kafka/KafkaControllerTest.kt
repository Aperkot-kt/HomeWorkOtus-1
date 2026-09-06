package ru.parceldelivery.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Test
import ru.parceldelivery.api.v1.models.DeliveryCreateRequest
import ru.parceldelivery.api.v1.models.DeliveryCreateResponse
import ru.parceldelivery.api.v1.models.ParcelDimensions
import ru.parceldelivery.api.v1.models.RequestType
import ru.parceldelivery.api.v1.models.ResponseResult
import ru.parceldelivery.mappers.apiV1RequestSerialize
import ru.parceldelivery.mappers.apiV1ResponseDeserialize
import java.util.Collections
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import ru.parceldelivery.app.kafka.AppKafkaConfig
import ru.parceldelivery.app.kafka.AppKafkaConsumer


class KafkaControllerTest {

    @Test
    fun `create request is read from notification topic and response is written back`() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val notificationTopic = config.kafkaTopicNotification

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(notificationTopic, PARTITION)))
            consumer.addRecord(
                ConsumerRecord(
                    notificationTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        DeliveryCreateRequest(
                            requestType = RequestType.CREATE,
                            senderId = "CL-1001",
                            receiverId = "CL-1002",
                            weight = 5.5,
                            dimensions = ParcelDimensions(40.0, 30.0, 20.0),
                        ),
                    ),
                ),
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        startOffsets[TopicPartition(notificationTopic, PARTITION)] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result: DeliveryCreateResponse = apiV1ResponseDeserialize(message.value())
        assertEquals(notificationTopic, message.topic())
        assertEquals(ResponseResult.SUCCESS, result.result)
        assertNotNull(result.trackNumber)
        assertTrue(result.trackNumber!!.startsWith("PD-"))
    }

    companion object {
        const val PARTITION = 0
    }
}