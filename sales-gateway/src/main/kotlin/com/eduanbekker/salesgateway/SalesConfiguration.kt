package com.eduanbekker.salesgateway

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class SalesConfiguration {
    @Bean
    fun transaction() = Consumer<String> {
        println(it)
    }
}