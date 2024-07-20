package com.group76.doctor.controllers.v1

import com.group76.doctor.controllers.v1.mapping.UrlMapping
import com.group76.doctor.entities.request.CreateDoctorRequest
import com.group76.doctor.entities.request.UpdateDoctorRequest
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.usecases.ICreateDoctorUseCase
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
    private val createDoctorUseCase: ICreateDoctorUseCase,
    private val updateDoctorUseCase : IUpdateDoctorUseCase
) {
    @PostMapping(
        name = "CreateDoctor"
    )
    @Operation(
        method = "CreateDoctor",
        description = "Create a doctor",
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
    fun createDoctor(
        @Valid @RequestBody request: CreateDoctorRequest
    ): ResponseEntity<Any> {
        val response = createDoctorUseCase.execute(request)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }

    @PutMapping(
        name = "UpdateDoctor"
    )
    @Operation(
        method = "UpdateDoctor",
        description = "Update a doctor",
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
    fun updateDoctor(
        @Valid @RequestBody request: UpdateDoctorRequest,
        @RequestHeader(value = "Authorization") auth: String
    ): ResponseEntity<Any> {
        val response = updateDoctorUseCase.execute(request, auth)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }
}