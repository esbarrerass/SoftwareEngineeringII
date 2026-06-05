package com.interrapidisimo.pruebaandroid.data.remote.api

import com.interrapidisimo.pruebaandroid.data.remote.model.AuthRequest
import com.interrapidisimo.pruebaandroid.data.remote.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Auth endpoint served from apitesting.interrapidisimo.co/FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas
 */
interface SeguridadApi {

    @POST("api/Seguridad/AuthenticaUsuarioApp")
    suspend fun authenticaUsuario(
        @Header("Usuario") usuario: String,
        @Header("Identificacion") identificacion: String,
        @Header("Accept") accept: String = "text/json",
        @Header("IdUsuario") idUsuario: String,
        @Header("IdCentroServicio") idCentroServicio: String,
        @Header("NombreCentroServicio") nombreCentroServicio: String,
        @Header("IdAplicativoOrigen") idAplicativoOrigen: String,
        @Body body: AuthRequest
    ): Response<AuthResponse>
}
