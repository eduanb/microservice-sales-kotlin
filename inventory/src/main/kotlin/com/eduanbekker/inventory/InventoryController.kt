package com.eduanbekker.inventory

import com.eduanbekker.inventory.domain.Inventory
import com.eduanbekker.inventory.domain.InventoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class InventoryController(val inventoryRepository: InventoryRepository) {

    @GetMapping("/inventory")
    fun getInventory(): MutableIterable<Inventory> = inventoryRepository.findAll()

    @GetMapping("/inventory/{id}")
    fun getInventory(@PathVariable id: Long): ResponseEntity<Any> =
        inventoryRepository.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}