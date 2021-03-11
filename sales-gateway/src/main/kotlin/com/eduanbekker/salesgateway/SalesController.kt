package com.eduanbekker.salesgateway

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SalesController(val inventoryClient: InventoryClient,
                      val streamBridge: StreamBridge) {

    @PostMapping("/buy")
    fun buy(@RequestParam account: Long, @RequestParam item: Long) {
        val inventoryItem = inventoryClient.getItem(item)
        streamBridge.send("updateAccount-in-0", AccountChangeRequest(account, inventoryItem.price, ChangeType.SUBTRACT))
        streamBridge.send("updateInventory-in-0", InventoryUpdateRequest(account, 1, ChangeType.SUBTRACT))
    }

}

data class AccountChangeRequest(
    val id: Long,
    val amount: Double,
    val type: ChangeType
)

data class InventoryUpdateRequest(
    val id: Long,
    val amount: Int,
    val type: ChangeType
)

enum class ChangeType {
    ADD, SUBTRACT
}