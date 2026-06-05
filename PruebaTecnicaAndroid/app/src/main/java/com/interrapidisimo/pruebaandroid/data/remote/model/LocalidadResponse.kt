package com.interrapidisimo.pruebaandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class LocalidadResponse(
    @SerializedName("IdLocalidad") val idLocalidad: String?,
    @SerializedName("AbreviacionCiudad") val abreviacionCiudad: String?,
    @SerializedName("NombreCompleto") val nombreCompleto: String?,
    @SerializedName("NombreCorto") val nombreCorto: String?
)
