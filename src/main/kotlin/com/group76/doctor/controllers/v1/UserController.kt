package com.group76.doctor.controllers.v1

import com.group76.doctor.controllers.v1.mapping.UrlMapping
import com.group76.doctor.entities.request.CreateDoctorRequest
import com.group76.doctor.entities.request.GetDoctorBySpecialtyRequest
import com.group76.doctor.entities.request.UpdateDoctorRequest
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.usecases.ICreateDoctorUseCase
import com.group76.doctor.usecases.IGetDoctorUseCase
import com.group76.doctor.usecases.IUpdateDoctorUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UrlMapping.Version.V1 + UrlMapping.Resource.USER)
class UserController(
    private val createClientUseCase: ICreateDoctorUseCase,
    private val getClientUseCase: IGetDoctorUseCase,
    private val updateClientUseCase : IUpdateDoctorUseCase
) {
    @PostMapping(
        name = "CreateClient"
    )
    @Operation(
        method = "CreateClient",
        description = "Create a client",
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
    fun createClient(
        @Valid @RequestBody request: CreateDoctorRequest
    ): ResponseEntity<Any> {
        val response = createClientUseCase.execute(request)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }

    @GetMapping(
        name = "GetClient"
    )
    @Operation(
        method = "GetClient",
        description = "Get a client information",
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
            ),
            ApiResponse(
                description = "Forbidden", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun getClient(
        @RequestHeader(value = "Authorization") auth: String
    ): ResponseEntity<Any> {
        val response = getClientUseCase.execute(GetDoctorBySpecialtyRequest(auth))

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }

    @PutMapping(
        name = "UpdateClient"
    )
    @Operation(
        method = "UpdateClient",
        description = "Update a client",
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
    fun updateClient(
        @Valid @RequestBody request: UpdateDoctorRequest,
        @RequestHeader(value = "Authorization") auth: String
    ): ResponseEntity<Any> {
        val response = updateClientUseCase.execute(request, auth)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }
}