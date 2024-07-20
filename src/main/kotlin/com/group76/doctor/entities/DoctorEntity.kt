package com.group76.doctor.entities

import java.util.UUID

data class DoctorEntity(
    val id: UUID,
    val crm: String,
    val password: String,
    val email: String,
    val name: String,
    val address: String,
    val cep: String,
    val state: String,
    val city: String,
    val phone: String,
    val medicalSpecialty: String
)
