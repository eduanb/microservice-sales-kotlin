package com.eduanbekker.inventory

import com.eduanbekker.inventory.domain.InventoryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import java.util.function.Function

@Configuration
class InventoryConfiguration {

    /**
     * Note this implementation is intentionally Java-like for demonstration purposes.
     * This is in contrast to AccountConfiguration which shows of Kotlin features like
     * Extension methods, scoped functions, elvis operator etc.
     */
    @Bean
    fun updateInventory(inventoryRepository: InventoryRepository) = Function<InventoryUpdateRequest, Any> { request ->
        val item = inventoryRepository.findByIdOrNull(request.id)
        if (item == null) {
            return@Function "Could not find item: ${request.id}"
        }

        println("Updating inventory: $item")

        if (request.type == ChangeType.ADD) {
            item.total = item.total + request.amount
        } else {
            item.total = item.total - request.amount
        }

        println("Inventory is now: $item")
        inventoryRepository.save(item)

    }
}