package com.eduanbekker.inventory

data class InventoryUpdateRequest(
    val id: Long,
    val amount: Int,
    val type: ChangeType
)

enum class ChangeType {
    ADD, SUBTRACT
}