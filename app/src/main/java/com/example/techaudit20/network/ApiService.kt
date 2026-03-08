package com.example.techaudit20.network

import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("equipos") // Cambiado a minúsculas para coincidir exactamente con el recurso en MockAPI
    suspend fun enviarEquipo(
        @Body equipo: AuditItem
    ): Response<AuditItem>

    @POST("laboratorios")
    suspend fun enviarLaboratorio(
        @Body lab: Laboratorio
    ): Response<Laboratorio>
}
