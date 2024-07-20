package com.group76.doctor.services.impl

import com.group76.doctor.configuration.SystemProperties
import com.group76.doctor.services.IMedicalSpecialtyDbService
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import software.amazon.awssdk.services.dynamodb.model.ScanResponse

@Component
class MedicalSpecialtyDbServiceImpl(
    private val systemProperties: SystemProperties
): IMedicalSpecialtyDbService {
    private val tableName = systemProperties.collection.medicalSpecialties
    override fun getAll(): ScanResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val scanRequest = ScanRequest.builder()
            .tableName(tableName)
            .build()

        val result = dynamoDbClient.scan(scanRequest)
        dynamoDbClient.close()
        return result
    }

    override fun verify(medicalSpecialty: String): Boolean {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val queryRequest = QueryRequest.builder()
            .tableName(tableName)
            .indexName("specialty")
            .keyConditionExpression("secondaryKey = :v_key")
            .expressionAttributeValues(mapOf(
                ":v_key" to software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().s(medicalSpecialty).build()
            ))
            .limit(1)
            .build()

        val response: QueryResponse = dynamoDbClient.query(queryRequest)
        dynamoDbClient.close()
        return response.hasItems()
    }
}