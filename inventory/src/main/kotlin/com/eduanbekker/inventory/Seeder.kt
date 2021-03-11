package com.eduanbekker.inventory

import com.eduanbekker.inventory.domain.Inventory
import com.eduanbekker.inventory.domain.InventoryRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["seed"], havingValue = "true")
class Seeder {
    @Bean
    fun seed(inventoryRepository: InventoryRepository) = CommandLineRunner {
        if(inventoryRepository.count() == 0L) {
            inventoryRepository.save(Inventory(1, "PS5", 10, 499.99))
        }
    }
}