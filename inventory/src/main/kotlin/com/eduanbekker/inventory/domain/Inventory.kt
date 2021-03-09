package com.eduanbekker.inventory.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Inventory(
    @Id
    var id: Long = 0,
    var name: String,
    var total: Int
)