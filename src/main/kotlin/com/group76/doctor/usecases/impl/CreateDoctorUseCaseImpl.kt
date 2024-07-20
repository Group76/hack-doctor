package com.group76.doctor.usecases.impl

import com.group76.doctor.configuration.SystemProperties
import com.group76.doctor.entities.DoctorEntity
import com.group76.doctor.entities.request.CreateDoctorRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.services.IDoctorDbService
import com.group76.doctor.services.IHashService
import com.group76.doctor.services.IJwtService
import com.group76.doctor.services.IMedicalSpecialtyDbService
import com.group76.doctor.usecases.ICreateDoctorUseCase
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateDoctorUseCaseImpl(
    private val doctorDb: IDoctorDbService,
    private val hashService: IHashService,
    private val jtwService: IJwtService,
    private val medicalSpecialtyDbService: IMedicalSpecialtyDbService
) : ICreateDoctorUseCase {
    override fun execute(payload: CreateDoctorRequest): BaseResponse<GetDoctorInformationResponse> {
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

        val doctor = DoctorEntity(
            name = payload.name,
            id = UUID.randomUUID(),
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

        val result = doctorDb.putItem(doctor)

        if(!result.sdkHttpResponse().isSuccessful){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Error while inserting doctor."),
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
                token = jtwService.generateToken(doctor.id.toString()),
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