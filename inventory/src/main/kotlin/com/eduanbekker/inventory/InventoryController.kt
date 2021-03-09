package com.eduanbekker.inventory

import com.eduanbekker.inventory.domain.Inventory
import com.eduanbekker.inventory.domain.InventoryRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InventoryController(val inventoryRepository: InventoryRepository) {
    @GetMapping("/inventory")
    fun getInventory(): MutableIterable<Inventory> = inventoryRepository.findAll()
}