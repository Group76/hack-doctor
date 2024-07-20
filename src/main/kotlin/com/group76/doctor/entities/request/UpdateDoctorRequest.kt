package com.group76.doctor.entities.request

import com.group76.doctor.utils.BrazilStates
import com.group76.doctor.utils.CepValidator
import java.util.*

data class UpdateDoctorRequest(
    val id: String,
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
) {
    fun getError(): String? {
        try {
            UUID.fromString(id)
        } catch (ex: Exception) {
            return "Invalid ID"
        }

        if (password.length < 8)
            return "Password not long enough, should be at least 8 characters"

        if (crm.isEmpty())
            return "CRM must be informed"

        if (name.isEmpty())
            return "Name must be informed"

        if (email.isEmpty())
            return "Email must be informed"

        if (address.isEmpty())
            return "Address must be informed"

        if (cep.isEmpty())
            return "Cep must be informed"

        if (state.isEmpty())
            return "State must be informed"

        if (city.isEmpty())
            return "City must be informed"

        if (phone.isEmpty())
            return "Phone must be informed"

        if (medicalSpecialty.isEmpty())
            return "Medical Specialty must be informed"

        if (!crm.contains('-') || crm.length != 9)
            return "CRM must contain state"

        if (!BrazilStates.containState(crm))
            return "CRM must contain state"

        if (!CepValidator.validateCep(cep))
            return "CEP is invalid"

        return null
    }
}