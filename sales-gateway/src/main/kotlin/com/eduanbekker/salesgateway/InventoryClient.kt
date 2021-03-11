package com.eduanbekker.salesgateway

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(url = "http://localhost:8082", name = "inventory")
interface InventoryClient {
    @GetMapping("/inventory/{id}")
    fun getItem(@PathVariable id: Long): Item
}

data class Item(
    var id: Long = 0,
    var name: String,
    var total: Int,
    var price: Double
)
