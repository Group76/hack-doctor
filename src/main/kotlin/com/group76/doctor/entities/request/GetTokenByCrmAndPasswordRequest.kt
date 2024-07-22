package com.group76.doctor.entities.request

data class GetTokenByCrmAndPasswordRequest(
    val crm: String,
    val password: String
)
