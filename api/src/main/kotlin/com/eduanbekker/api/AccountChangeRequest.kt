package com.eduanbekker.api

data class AccountChangeRequest(
    val id: Long,
    val amount: Double,
    val type: ChangeType
)