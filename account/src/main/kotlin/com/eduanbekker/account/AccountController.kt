package com.eduanbekker.account

import com.eduanbekker.account.domain.Account
import com.eduanbekker.account.domain.AccountRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(val accountRepository: AccountRepository) {
    @GetMapping("/accounts")
    fun getAccounts(): MutableIterable<Account> = accountRepository.findAll()
}