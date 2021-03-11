package com.eduanbekker.account

import com.eduanbekker.account.domain.AccountRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.Message
import java.util.function.Function

@Configuration
class AccountConfiguration {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * Note this implementation intentionally uses a lot of Kotlin features for demonstration purposes.
     * These include extension methods, scoped functions, elvis operator etc
     * This is in contrast to the implementation in InventoryConfiguration
     */
    @Bean
    fun updateAccount(accountRepository: AccountRepository) = Function<Message<AccountChangeRequest>, Any> { request ->
        accountRepository.findByIdOrNull(request.payload.id)
            ?.also { logger.info("Updating account: $it") }
            ?.apply { balance = balance.change(request.payload.type, request.payload.amount) }
            ?.let { logger.info("Account is now: $it"); accountRepository.save(it) }

            ?: "Could not find account: ${request.payload.id}"
                .also { logger.info(it) }
    }
}

fun Double.change(type: ChangeType, amount: Double) = when (type) {
    ChangeType.ADD -> plus(amount)
    ChangeType.SUBTRACT -> minus(amount)
}