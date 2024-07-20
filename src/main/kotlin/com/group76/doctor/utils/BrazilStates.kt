package com.group76.doctor.utils

class BrazilStates {
    companion object {
        private val states = setOf(
            "AC", "AL", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MG",
            "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO",
            "RR", "RS", "SC", "SE", "SP", "TO"
        )

        fun containState(input: String): Boolean {
            return states.any { state -> input.contains(state, ignoreCase = true) }
        }
    }
}