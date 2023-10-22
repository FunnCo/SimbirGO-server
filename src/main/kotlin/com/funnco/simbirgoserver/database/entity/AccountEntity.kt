package com.funnco.simbirgoserver.database.entity

import org.springframework.data.annotation.Id

data class AccountEntity(
    @Id
    var id: String? = null,
    var username: String? = null,
    var roleId: Int? = null,
    var balance: Double? = null,
    var hashedPassword: String? = null,
)

