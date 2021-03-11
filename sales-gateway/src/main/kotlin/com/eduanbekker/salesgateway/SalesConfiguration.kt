package com.eduanbekker.salesgateway

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
class SalesConfiguration {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun transaction() = Consumer<Message<String>> {
        logger.info(it.payload)
    }
}