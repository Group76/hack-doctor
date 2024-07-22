package com.group76.doctor.usecases

import com.group76.doctor.entities.request.GetDoctorsByFilterRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse

interface IGetDoctorsByFilterUseCase {
    fun execute(
        payload: GetDoctorsByFilterRequest
    ): BaseResponse<List<GetDoctorInformationResponse>>
}