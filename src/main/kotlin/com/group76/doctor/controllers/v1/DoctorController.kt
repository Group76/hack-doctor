package com.group76.doctor.controllers.v1

import com.group76.doctor.controllers.v1.mapping.UrlMapping
import com.group76.doctor.entities.request.CreateDoctorRequest
import com.group76.doctor.entities.request.GetDoctorsByFilterRequest
import com.group76.doctor.entities.request.UpdateDoctorRequest
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.usecases.ICreateDoctorUseCase
import com.group76.doctor.usecases.IGetDoctorsByFilterUseCase
import com.group76.doctor.usecases.IUpdateDoctorUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UrlMapping.Version.V1 + UrlMapping.Resource.DOCTOR)
class DoctorController(
    private val getDoctorsByFilterUseCase: IGetDoctorsByFilterUseCase
) {
    @GetMapping(
        name = "GetDoctorsByFilter"
    )
    @Operation(
        method = "GetDoctorsByFilter",
        description = "Get doctors by filter",
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
    fun getDoctorsByFilter(
        @Valid @RequestBody request: GetDoctorsByFilterRequest
    ): ResponseEntity<Any> {
        val response = getDoctorsByFilterUseCase.execute(request)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }
}