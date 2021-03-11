package com.eduanbekker.account

import com.eduanbekker.account.domain.Account
import com.eduanbekker.account.domain.AccountRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["seed"], havingValue = "true")
class Seeder {
    @Bean
    fun seed(accountRepository: AccountRepository) = CommandLineRunner {
        if (accountRepository.count() == 0L) {
            accountRepository.save(Account(1, 100.0))
        }
    }
}