package com.group76.doctor.usecases

import com.group76.doctor.entities.request.GetTokenByCrmAndPasswordRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetTokenResponse

interface IGetTokenByCrmAndPasswordUseCase {
    fun execute(
     payload: GetTokenByCrmAndPasswordRequest
    ): BaseResponse<GetTokenResponse>
}