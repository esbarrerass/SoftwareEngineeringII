package com.interrapidisimo.pruebaandroid.domain.model

data class Tabla(
    val nombreTabla: String,
    val pk: String?,
    val queryCreacion: String?,
    val batchSize: Int?,
    val filtro: String?,
    val error: String?,
    val numeroCampos: Int?,
    val metodoApp: String?,
    val fechaActualizacionSincro: String?
)
