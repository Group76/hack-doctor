package com.group76.doctor.entities.request

data class GetDoctorsByFilterRequest (
    val specialty: String,
    val city: String,
    val state: String
)