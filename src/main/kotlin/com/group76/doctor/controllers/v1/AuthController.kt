package com.group76.doctor.controllers.v1

import com.group76.doctor.controllers.v1.mapping.UrlMapping
import com.group76.doctor.entities.request.GetTokenByCrmAndPassword
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.usecases.IGetTokenByCrmAndPasswordUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UrlMapping.Version.V1 + UrlMapping.Resource.AUTH)
class AuthController(
    private val getTokenByCrmAndPasswordUseCase: IGetTokenByCrmAndPasswordUseCase
) {
    @PostMapping(
        name = "GetTokenByCrmAndPassword"
    )
    @Operation(
        method = "GetTokenByCrmAndPassword",
        description = "Get a token by CRM and password",
        responses = [
            ApiResponse(
                description = "OK", responseCode = "200", content = [
                    Content(schema = Schema(implementation = GetDoctorInformationResponse::class))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun getTokenByCrmAndEmail(
        @Valid @RequestBody request: GetTokenByCrmAndPassword
    ): ResponseEntity<Any> {
        val response = getTokenByCrmAndPasswordUseCase.execute(request)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }
}