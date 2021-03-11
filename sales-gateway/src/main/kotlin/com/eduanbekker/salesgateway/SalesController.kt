package com.eduanbekker.salesgateway

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SalesController(val inventoryClient: InventoryClient,
                      val streamBridge: StreamBridge) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/buy")
    fun buy(@RequestParam account: Long, @RequestParam item: Long) {
        logger.info("Request to buy item: $item for account $account")
        val inventoryItem = inventoryClient.getItem(item)
        streamBridge.send("updateAccount-in-0",
            MessageBuilder.withPayload(AccountChangeRequest(account, inventoryItem.price, ChangeType.SUBTRACT)).build())
        streamBridge.send("updateInventory-in-0",
            MessageBuilder.withPayload(InventoryUpdateRequest(account, 1, ChangeType.SUBTRACT)).build())
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