package com.group76.doctor.usecases.impl

import com.group76.doctor.entities.DoctorEntity
import com.group76.doctor.entities.request.GetDoctorBySpecialtyRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.services.IDoctorDbService
import com.group76.doctor.services.IJwtService
import com.group76.doctor.usecases.IGetDoctorUseCase
import com.group76.doctor.utils.Helper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetDoctorUseCaseImpl(
    private val dynamo: IDoctorDbService,
    private val jwtService: IJwtService
) : IGetDoctorUseCase {
    override fun execute(payload: GetDoctorBySpecialtyRequest): BaseResponse<GetDoctorInformationResponse> {
        val id = jwtService.extractId(payload.token)

        if(id == null
            || jwtService.isExpired(payload.token)) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Unauthorized"),
                HttpStatus.UNAUTHORIZED
            )
        }

        if(id == Helper.getGuestId().toString()) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Forbidden"),
                HttpStatus.FORBIDDEN
            )
        }

        val result = dynamo.getById(id)

        if(!result.sdkHttpResponse().isSuccessful){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Error while updating client."),
                statusCodes = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        if(!result.hasItem()){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Client not found."),
                statusCodes = HttpStatus.BAD_REQUEST
            )
        }

        val item = result.item()

        val client = DoctorEntity(
            name = item["user_name"]?.s(),
            id = UUID.fromString(item["id"]?.s()),
            email = item["email"]?.s(),
            phone = item["phone"]?.s(),
            document = item["document"]?.s(),
            password = null,
            address = item["address"]?.s()
        )

        return BaseResponse(
            data = GetDoctorInformationResponse(
                email = client.email,
                phone = client.phone,
                name = client.name,
                address = client.address,
                id = client.id,
                document = client.document,
                token = null
            ),
            error = null
        )
    }
}