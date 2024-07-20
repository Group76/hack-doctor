package com.group76.doctor.entities.request

data class GetTokenByCrmAndPassword(
    val crm: String,
    val password: String
)
