package com.group76.doctor.usecases.impl

import com.group76.doctor.entities.request.GetTokenByCrmAndPassword
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetTokenResponse
import com.group76.doctor.services.IDoctorDbService
import com.group76.doctor.services.IHashService
import com.group76.doctor.services.IJwtService
import com.group76.doctor.usecases.IGetTokenByCrmAndPasswordUseCase
import com.group76.doctor.utils.Helper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class GetTokenByCrmAndPasswordUseCaseImpl(
    private val dynamo: IDoctorDbService,
    private val hashService: IHashService,
    private val jtwService: IJwtService
) : IGetTokenByCrmAndPasswordUseCase {
    override fun execute(payload: GetTokenByCrmAndPassword): BaseResponse<GetTokenResponse> {
        val scanResponse = dynamo
            .getByCrm(payload.crm)

        if (!scanResponse.hasItems())
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Doctor not found."),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        val item = scanResponse.items().first()
        val password = item["password"]?.s()
        val id = item["id"]?.s()

        if (password.isNullOrEmpty()
            || id.isNullOrEmpty()
        ) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Doctor not found."),
                statusCodes = HttpStatus.BAD_REQUEST
            )
        }

        if (!hashService.checkPassword(payload.password, password)) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Doctor not authorized."),
                statusCodes = HttpStatus.UNAUTHORIZED
            )
        }

        return BaseResponse(
            data = GetTokenResponse(
                token = jtwService.generateToken(id)!!,
                id = id
            ),
            error = null
        )
    }
}