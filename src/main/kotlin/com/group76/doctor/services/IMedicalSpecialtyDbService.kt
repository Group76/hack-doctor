package com.group76.doctor.services

import com.group76.doctor.entities.DoctorEntity
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse
import software.amazon.awssdk.services.dynamodb.model.ScanResponse
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse

interface IMedicalSpecialtyDbService {
    fun getAll(): ScanResponse
    fun verify(medicalSpecialty: String): Boolean
}