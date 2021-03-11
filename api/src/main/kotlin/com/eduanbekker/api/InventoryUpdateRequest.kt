package com.eduanbekker.api

data class InventoryUpdateRequest(
    val id: Long,
    val amount: Int,
    val type: ChangeType
)