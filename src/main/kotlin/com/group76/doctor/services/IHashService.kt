package com.group76.doctor.services

interface IHashService {
    fun hash(value: String): String
    fun checkPassword(plainPassword: String, hashedPassword: String): Boolean
}
