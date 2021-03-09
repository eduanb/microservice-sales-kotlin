package com.eduanbekker.account

data class AccountChangeRequest(
    val id: Long,
    val amount: Double,
    val type: ChangeType
)

enum class ChangeType {
    ADD, SUBTRACT
}