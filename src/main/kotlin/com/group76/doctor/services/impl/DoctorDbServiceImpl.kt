package com.group76.doctor.services.impl

import com.group76.doctor.configuration.SystemProperties
import com.group76.doctor.entities.DoctorEntity
import com.group76.doctor.services.IDoctorDbService
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*

@Component
class DoctorDbServiceImpl(
    private val systemProperties: SystemProperties
) : IDoctorDbService {
    private val tableName = systemProperties.collection.doctor
    override fun putItem(doctorEntity: DoctorEntity) : PutItemResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val itemValues = mutableMapOf(
            "id" to AttributeValue.builder().s(doctorEntity.id.toString()).build(),
            "password" to AttributeValue.builder().s(doctorEntity.password).build(),
            "crm" to AttributeValue.builder().s(doctorEntity.crm).build(),
            "email" to AttributeValue.builder().s(doctorEntity.email).build(),
            "name" to AttributeValue.builder().s(doctorEntity.name).build(),
            "address" to AttributeValue.builder().s(doctorEntity.address).build(),
            "cep" to AttributeValue.builder().s(doctorEntity.cep).build(),
            "state" to AttributeValue.builder().s(doctorEntity.state).build(),
            "city" to AttributeValue.builder().s(doctorEntity.city).build(),
            "phone" to AttributeValue.builder().s(doctorEntity.phone).build(),
            "medicalSpecialty" to AttributeValue.builder().s(doctorEntity.medicalSpecialty).build()
        )

        val putItemRequest = PutItemRequest.builder()
            .tableName(tableName)
            .item(itemValues)
            .build()

        val response = dynamoDbClient.putItem(putItemRequest)
        dynamoDbClient.close()
        return response
    }

    override fun verifyCrm(crm: String, id: String?): Boolean {
        return scan("crm", crm, "id", id).count() > 0
    }

    override fun getById(id: String): GetItemResponse {
        val key = mapOf("id" to AttributeValue.builder().s(id).build())
        val client = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val updateRequest = GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build()

        val item = client.getItem(updateRequest)
        client.close()
        return item
    }

    override fun getByCrm(crm: String): ScanResponse {
        return scan("crm", crm)
    }

    override fun getByMedicalSpecialty(medicalSpecialty: String): ScanResponse {
        return scan("medicalSpecialty", medicalSpecialty)
    }

    fun scan(
        attributeName: String,
        value: String,
        attributeNameDiff: String? = null,
        valueNotEqual: String? = null
    ): ScanResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val scanRequest = if(attributeNameDiff == null || valueNotEqual == null)
            ScanRequest.builder()
            .tableName(tableName)
            .filterExpression("#attr = :value")
            .expressionAttributeNames(mapOf("#attr" to attributeName))
            .expressionAttributeValues(mapOf(":value" to AttributeValue.builder().s(value).build()))
            .build()
        else ScanRequest.builder()
            .tableName(tableName)
            .filterExpression("#attr = :value AND #notEqualAttr <> :notEqualValue")
            .expressionAttributeNames(mapOf(
                "#attr" to attributeName,
                "#notEqualAttr" to attributeNameDiff
            ))
            .expressionAttributeValues(mapOf(
                ":value" to AttributeValue.builder().s(value).build(),
                ":notEqualValue" to AttributeValue.builder().s(valueNotEqual).build()
            ))
            .build()

        val result = dynamoDbClient.scan(scanRequest)
        dynamoDbClient.close()
        return result
    }

    override fun updateItem(doctorEntity: DoctorEntity): UpdateItemResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val updateExpression = "SET password = :password" +
                ", crm = :crm" +
                ", email = :email" +
                ", name = :name" +
                ", address = :address" +
                ", cep = :cep" +
                ", state = :state" +
                ", city = :city" +
                ", phone = :phone" +
                ", medicalSpecialty = :medicalSpecialty"

        val itemValues = mutableMapOf(
            ":password" to AttributeValue.builder().s(doctorEntity.password).build(),
            ":crm" to AttributeValue.builder().s(doctorEntity.crm).build(),
            ":email" to AttributeValue.builder().s(doctorEntity.email).build(),
            ":name" to AttributeValue.builder().s(doctorEntity.name).build(),
            ":address" to AttributeValue.builder().s(doctorEntity.address).build(),
            ":cep" to AttributeValue.builder().s(doctorEntity.cep).build(),
            ":state" to AttributeValue.builder().s(doctorEntity.state).build(),
            ":city" to AttributeValue.builder().s(doctorEntity.city).build(),
            ":phone" to AttributeValue.builder().s(doctorEntity.phone).build(),
            ":medicalSpecialty" to AttributeValue.builder().s(doctorEntity.medicalSpecialty).build()
        )

        val updateItemRequest = UpdateItemRequest.builder()
            .tableName(tableName)
            .key(mapOf("id" to AttributeValue.builder().s(doctorEntity.id.toString()).build()))
            .updateExpression(updateExpression)
            .expressionAttributeValues(itemValues)
            .returnValues(ReturnValue.UPDATED_NEW)
            .build()

        val result = dynamoDbClient.updateItem(updateItemRequest)
        dynamoDbClient.close()
        return result
    }
}