package com.eduanbekker.salesgateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SalesGateway

fun main(args: Array<String>) {
    runApplication<SalesGateway>(*args)
}
