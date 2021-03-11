package com.eduanbekker.inventory

import com.eduanbekker.api.InventoryClient
import com.eduanbekker.api.Item
import com.eduanbekker.inventory.domain.Inventory
import com.eduanbekker.inventory.domain.InventoryRepository
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Primary
class InventoryController(val inventoryRepository: InventoryRepository): InventoryClient {

    @GetMapping("/inventory")
    fun getInventory(): MutableIterable<Inventory> = inventoryRepository.findAll()

    @GetMapping("/inventory/{id}")
    override fun getItem(@PathVariable id: Long): ResponseEntity<Item> =
        inventoryRepository.findByIdOrNull(id)
            ?.let { Item(it.id, it.name, it.total, it.price)  }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}