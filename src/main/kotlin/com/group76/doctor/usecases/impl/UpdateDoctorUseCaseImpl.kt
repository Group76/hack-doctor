package com.group76.doctor.usecases.impl

import com.group76.doctor.entities.DoctorEntity
import com.group76.doctor.entities.request.UpdateDoctorRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.services.IDoctorDbService
import com.group76.doctor.services.IHashService
import com.group76.doctor.services.IJwtService
import com.group76.doctor.services.IMedicalSpecialtyDbService
import com.group76.doctor.usecases.IUpdateDoctorUseCase
import com.group76.doctor.utils.Helper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class UpdateDoctorUseCaseImpl(
    private val doctorDb: IDoctorDbService,
    private val hashService: IHashService,
    private val jwtService: IJwtService,
    private val medicalSpecialtyDbService: IMedicalSpecialtyDbService
) : IUpdateDoctorUseCase {
    override fun execute(payload: UpdateDoctorRequest, token: String): BaseResponse<GetDoctorInformationResponse> {
        val error = payload.getError()

        if(error != null)
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError(error),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        if(!medicalSpecialtyDbService.verify(payload.medicalSpecialty)){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Medical specialty not valid."),
                statusCodes = HttpStatus.BAD_REQUEST
            )
        }

        val id = jwtService.extractId(token)

        if(id == null
            || jwtService.isExpired(token)) {
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

        val doctor = DoctorEntity(
            name = payload.name,
            id = UUID.fromString(id),
            email = payload.email,
            phone = payload.phone,
            password = hashService.hash(payload.password),
            address = payload.address,
            medicalSpecialty = payload.medicalSpecialty,
            city = payload.city,
            cep = payload.cep,
            crm = payload.crm,
            state = payload.state
        )

        if(doctorDb.verifyCrm(doctor.crm))
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("CRM already exists."),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        val response = doctorDb.updateItem(doctor)

        if(!response.sdkHttpResponse().isSuccessful){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Error while updating client."),
                statusCodes = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        return BaseResponse(
            data = GetDoctorInformationResponse(
                email = doctor.email,
                phone = doctor.phone,
                name = doctor.name,
                address = doctor.address,
                id = doctor.id,
                token = jwtService.generateToken(doctor.id.toString()),
                medicalSpecialty = doctor.medicalSpecialty,
                city = doctor.city,
                cep = doctor.cep,
                crm = doctor.crm,
                state = doctor.state
            ),
            error = null
        )
    }
}