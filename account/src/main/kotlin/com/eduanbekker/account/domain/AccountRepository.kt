package com.eduanbekker.account.domain

import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account, Long>