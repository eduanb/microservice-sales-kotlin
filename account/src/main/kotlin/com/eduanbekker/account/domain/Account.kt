package com.eduanbekker.account.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
    @Id
    var id: Long = 0,
    var balance: Double = 0.0
)