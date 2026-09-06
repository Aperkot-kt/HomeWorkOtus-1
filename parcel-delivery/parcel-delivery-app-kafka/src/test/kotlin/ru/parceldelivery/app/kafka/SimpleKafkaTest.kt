package ru.parceldelivery.app.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.Properties
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Интеграционный тест записи и вычитки из топика `notification`.
 *
 * Для запуска требуется поднять Кафку (docker-compose) и установить
 * переменную окружения KAFKA_HOSTS=localhost:9092
 */
@Disabled("Requires running Kafka broker")
class SimpleKafkaTest {

    @Test
    fun `write and read message from notification topic`() {
        val config = AppKafkaConfig()
        val topic = config.kafkaTopicNotification

        val payload = """{"requestType":"read","trackNumber":"PD-2026-000001"}"""
        val producer = config.createKafkaProducer()
        try {
            producer.send(ProducerRecord(topic, UUID.randomUUID().toString(), payload)).get()
        } finally {
            producer.close()
        }

        val props = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaHosts)
            put(ConsumerConfig.GROUP_ID_CONFIG, "simple-kafka-test-${UUID.randomUUID()}")
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        }

        val received = mutableListOf<String>()
        KafkaConsumer<String, String>(props).use { consumer ->
            consumer.subscribe(listOf(topic))
            val deadline = System.currentTimeMillis() + 30_000
            while (received.isEmpty() && System.currentTimeMillis() < deadline) {
                consumer.poll(Duration.ofMillis(500)).forEach {
                    received.add(it.value())
                }
            }
        }

        assertTrue(received.isNotEmpty(), "No message received from topic $topic")
        assertEquals(payload, received.first())
    }
}