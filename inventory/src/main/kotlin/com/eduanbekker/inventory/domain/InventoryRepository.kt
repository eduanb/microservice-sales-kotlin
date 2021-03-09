package com.eduanbekker.inventory.domain

import org.springframework.data.repository.CrudRepository

interface InventoryRepository : CrudRepository<Inventory, Long>