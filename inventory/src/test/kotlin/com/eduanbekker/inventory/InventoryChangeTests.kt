package com.eduanbekker.inventory

import com.eduanbekker.inventory.domain.Inventory
import com.eduanbekker.inventory.domain.InventoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * This is a full end to end integration test making use of https://www.testcontainers.org/
 * It starts up a full Postgres and full RabbitMQ
 * The test is on the RabbitMQ level. Sending an actual message and checking the response from RabbitMQ
 */
@SpringBootTest
@Testcontainers
class InventoryChangeTests {

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var inventoryRepository: InventoryRepository

    @Test
    fun notFound() {
        val body = """{"id": 99, "amount": 10, "type": "ADD"}"""
        rabbitTemplate.send(
            "updateInventory-in-0.inventory", Message(body.toByteArray(),
                MessageProperties().apply { contentType = "application/json" })
        )

        val result = rabbitTemplate.receive("output", 10_000)
        Assertions.assertEquals("Could not find item: 99", String(result!!.body))
    }

    @Test
    fun add() {
        inventoryRepository.save(Inventory(1, "PS5", 10))

        val body = """{"id": 1, "amount": 1, "type": "SUBTRACT"}"""
        rabbitTemplate.send(
            "updateInventory-in-0.inventory", Message(body.toByteArray(),
                MessageProperties().apply { contentType = "application/json" })
        )

        val result = rabbitTemplate.receive("output", 10_000)
        JSONAssert.assertEquals("""{"id":1,"name":"PS5","total":9}""", String(result!!.body), false)
    }

    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest")

        @Container
        private val rabbitMQContainer = RabbitMQContainer("rabbitmq:3-management")
            .withQueue("output")
            .withExchange("output", "topic")
            .withBinding("output", "output", mapOf(), "#", "queue")

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)

            registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost)
            registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort)
            registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername)
            registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword)
        }
    }
}
