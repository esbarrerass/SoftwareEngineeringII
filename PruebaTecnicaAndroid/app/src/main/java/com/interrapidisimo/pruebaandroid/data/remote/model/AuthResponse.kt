package com.interrapidisimo.pruebaandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("Usuario") val usuario: String?,
    @SerializedName("Identificacion") val identificacion: String?,
    @SerializedName("Nombre") val nombre: String?,
    @SerializedName("Apellido1") val apellido1: String?,
    @SerializedName("Apellido2") val apellido2: String?,
    @SerializedName("MensajeResultado") val mensajeResultado: Int?,
    @SerializedName("TokenJWT") val tokenJWT: String?
)
