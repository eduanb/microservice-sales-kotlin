package com.eduanbekker.api

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(url = "http://localhost:8082", name = "inventory", primary = false)
interface InventoryClient {
    @GetMapping("/inventory/{id}")
    fun getItem(@PathVariable id: Long): ResponseEntity<Item>
}

data class Item(
    var id: Long = 0,
    var name: String,
    var total: Int,
    var price: Double
)
