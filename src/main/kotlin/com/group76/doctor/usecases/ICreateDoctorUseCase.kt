package com.group76.doctor.usecases

import com.group76.doctor.entities.request.CreateDoctorRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse

interface ICreateDoctorUseCase {
    fun execute(
        payload: CreateDoctorRequest
    ): BaseResponse<GetDoctorInformationResponse>
}