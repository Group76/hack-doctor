package com.group76.doctor.usecases

import com.group76.doctor.entities.request.GetDoctorBySpecialtyRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse

interface IGetDoctorUseCase {
    fun execute(
        payload: GetDoctorBySpecialtyRequest
    ): BaseResponse<GetDoctorInformationResponse>
}