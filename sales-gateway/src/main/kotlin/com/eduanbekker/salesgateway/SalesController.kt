package com.eduanbekker.salesgateway

import com.eduanbekker.api.AccountChangeRequest
import com.eduanbekker.api.ChangeType
import com.eduanbekker.api.InventoryClient
import com.eduanbekker.api.InventoryUpdateRequest
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
            MessageBuilder.withPayload(AccountChangeRequest(account, inventoryItem.body!!.price, ChangeType.SUBTRACT)).build())
        streamBridge.send("updateInventory-in-0",
            MessageBuilder.withPayload(InventoryUpdateRequest(account, 1, ChangeType.SUBTRACT)).build())
    }

}