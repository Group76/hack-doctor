package com.group76.doctor.usecases

import com.group76.doctor.entities.request.UpdateDoctorRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse

interface IUpdateDoctorUseCase {
    fun execute(
        payload: UpdateDoctorRequest,
        token: String
    ): BaseResponse<GetDoctorInformationResponse>
}