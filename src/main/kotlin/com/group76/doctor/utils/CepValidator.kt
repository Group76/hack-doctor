package com.group76.doctor.utils

class CepValidator {
    companion object {
        fun validateCep(cep: String): Boolean {
            val regex = Regex("^\\d{5}-\\d{3}$|^\\d{8}$")
            return regex.matches(cep)
        }
    }
}