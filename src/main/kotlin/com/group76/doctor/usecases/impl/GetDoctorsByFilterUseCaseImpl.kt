package com.group76.doctor.usecases.impl

import com.group76.doctor.entities.request.GetDoctorsByFilterRequest
import com.group76.doctor.entities.response.BaseResponse
import com.group76.doctor.entities.response.GetDoctorInformationResponse
import com.group76.doctor.services.IDoctorDbService
import com.group76.doctor.usecases.IGetDoctorsByFilterUseCase
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.List

@Service
class GetDoctorsByFilterUseCaseImpl(
    private val doctorDb: IDoctorDbService,
): IGetDoctorsByFilterUseCase {
    override fun execute(
        payload: GetDoctorsByFilterRequest
    ): BaseResponse<List<GetDoctorInformationResponse>> {
        val result = doctorDb.getByMedicalSpecialtyAndCityState(
            medicalSpecialty = payload.specialty,
            city = payload.city,
            state = payload.state
        )

        if(!result.sdkHttpResponse().isSuccessful){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Error while getting doctors."),
                statusCodes = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        if(!result.hasItems()){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Doctors not found."),
                statusCodes = HttpStatus.OK
            )
        }

        return BaseResponse(
            data = result.items().map { doctor ->
                GetDoctorInformationResponse(
                    email = doctor["email"]!!.s(),
                    phone = doctor["phone"]!!.s(),
                    name = doctor["name"]!!.s(),
                    address = doctor["address"]!!.s(),
                    id = UUID.fromString(doctor["id"]!!.s()),
                    medicalSpecialty = doctor["medicalSpecialty"]!!.s(),
                    city = doctor["city"]!!.s(),
                    cep = doctor["cep"]!!.s(),
                    crm = doctor["crm"]!!.s(),
                    state = doctor["state"]!!.s(),
                    token = null
                )
            },
            error = null
        )
    }
}