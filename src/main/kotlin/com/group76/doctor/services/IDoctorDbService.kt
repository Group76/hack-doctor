package com.group76.doctor.services

import com.group76.doctor.entities.DoctorEntity
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse
import software.amazon.awssdk.services.dynamodb.model.ScanResponse
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse

interface IDoctorDbService {
    fun putItem(doctorEntity: DoctorEntity) : PutItemResponse
    fun verifyCrm(crm: String, id: String? = null): Boolean
    fun getById(id: String): GetItemResponse
    fun getByCrm(crm: String): ScanResponse
    fun getByMedicalSpecialty(medicalSpecialty: String): ScanResponse
    fun updateItem(doctorEntity: DoctorEntity) : UpdateItemResponse
}