package com.interrapidisimo.pruebaandroid.data.remote.api

import com.interrapidisimo.pruebaandroid.data.remote.model.LocalidadResponse
import com.interrapidisimo.pruebaandroid.data.remote.model.TablaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * API endpoints served from apitesting.interrapidisimo.co/apicontrollerpruebas
 */
interface ParametrosApi {

    @GET("api/ParametrosFramework/ConsultarParametrosFramework/{parametro}")
    suspend fun consultarParametro(
        @Path("parametro") parametro: String
    ): Response<String>

    @GET("api/SincronizadorDatos/ObtenerEsquema/{incluirDatos}")
    suspend fun obtenerEsquema(
        @Path("incluirDatos") incluirDatos: Boolean,
        @Header("Usuario") usuario: String,
        @Header("Identificacion") identificacion: String,
        @Header("IdUsuario") idUsuario: String,
        @Header("IdCentroServicio") idCentroServicio: String,
        @Header("NombreCentroServicio") nombreCentroServicio: String,
        @Header("IdAplicativoOrigen") idAplicativoOrigen: String
    ): Response<List<TablaResponse>>

    @GET("api/ParametrosFramework/ObtenerLocalidadesRecogidas")
    suspend fun obtenerLocalidades(): Response<List<LocalidadResponse>>
}
