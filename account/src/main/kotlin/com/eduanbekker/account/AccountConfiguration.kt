package com.eduanbekker.account

import com.eduanbekker.account.domain.AccountRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import java.util.function.Function

@Configuration
class AccountConfiguration {
    /**
     * Note this implementation intentionally uses a lot of Kotlin features for demonstration purposes.
     * These include extension methods, scoped functions, elvis operator etc
     * This is in contrast to the implementation in InventoryConfiguration
     */
    @Bean
    fun updateAccount(accountRepository: AccountRepository) = Function<AccountChangeRequest, Any> { request ->
        accountRepository.findByIdOrNull(request.id)
            ?.also { println("Updating account: $it") }
            ?.apply { balance = balance.change(request.type, request.amount) }
            ?.let { println("Account is now: $it"); accountRepository.save(it) }

            ?: "Could not find account: ${request.id}"
                .also { println(it) }
    }
}

fun Double.change(type: ChangeType, amount: Double) = when (type) {
    ChangeType.ADD -> plus(amount)
    ChangeType.SUBTRACT -> minus(amount)
}