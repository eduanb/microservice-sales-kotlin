package com.eduanbekker.inventory

import com.eduanbekker.api.ChangeType
import com.eduanbekker.api.InventoryUpdateRequest
import com.eduanbekker.inventory.domain.InventoryRepository
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.Message
import java.util.function.Function

@Configuration
@EntityScan
@ComponentScan
@EnableJpaRepositories
class InventoryConfiguration {

    /**
     * Note this implementation is intentionally Java-like for demonstration purposes.
     * This is in contrast to AccountConfiguration which shows of Kotlin features like
     * Extension methods, scoped functions, elvis operator etc.
     */
    @Bean
    fun updateInventory(inventoryRepository: InventoryRepository) = Function<Message<InventoryUpdateRequest>, Any> { request ->
        val item = inventoryRepository.findByIdOrNull(request.payload.id)
        if (item == null) {
            return@Function "Could not find item: ${request.payload.id}"
        }

        println("Updating inventory: $item")

        if (request.payload.type == ChangeType.ADD) {
            item.total = item.total + request.payload.amount
        } else {
            item.total = item.total - request.payload.amount
        }

        println("Inventory is now: $item")
        inventoryRepository.save(item)

    }
}