package com.interrapidisimo.pruebaandroid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tablas")
data class TablaEntity(
    @PrimaryKey val nombreTabla: String,
    val pk: String?,
    val queryCreacion: String?,
    val batchSize: Int?,
    val filtro: String?,
    val error: String?,
    val numeroCampos: Int?,
    val metodoApp: String?,
    val fechaActualizacionSincro: String?
)
