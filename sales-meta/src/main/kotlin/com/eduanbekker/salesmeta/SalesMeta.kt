package com.eduanbekker.salesmeta

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class SalesMeta

fun main(args: Array<String>) {
    runApplication<SalesMeta>(*args)
}
