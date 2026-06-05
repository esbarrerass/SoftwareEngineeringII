package com.interrapidisimo.pruebaandroid.data.repository

import com.interrapidisimo.pruebaandroid.data.local.dao.TablaDao
import com.interrapidisimo.pruebaandroid.data.local.entity.TablaEntity
import com.interrapidisimo.pruebaandroid.data.remote.api.ParametrosApi
import com.interrapidisimo.pruebaandroid.domain.model.Localidad
import com.interrapidisimo.pruebaandroid.util.AppConfig
import com.interrapidisimo.pruebaandroid.domain.model.Tabla
import com.interrapidisimo.pruebaandroid.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatosRepository @Inject constructor(
    private val parametrosApi: ParametrosApi,
    private val tablaDao: TablaDao
) {

    /**
     * Fetches the schema from remote, stores each table name in the local DB.
     * Requires the same auth headers as the login endpoint.
     */
    suspend fun sincronizarTablas(): Resource<Unit> {
        return try {
            val response = parametrosApi.obtenerEsquema(
                incluirDatos = true,
                usuario = AppConfig.USUARIO,
                identificacion = AppConfig.IDENTIFICACION,
                idUsuario = AppConfig.USUARIO,
                idCentroServicio = AppConfig.ID_CENTRO_SERVICIO,
                nombreCentroServicio = AppConfig.NOMBRE_CENTRO_SERVICIO,
                idAplicativoOrigen = AppConfig.ID_APLICATIVO_ORIGEN
            )
            if (response.isSuccessful) {
                val tablas = response.body() ?: emptyList()
                val entities = tablas.mapNotNull { tablaResponse ->
                    tablaResponse.nombreTabla?.let { nombre ->
                        TablaEntity(
                            nombreTabla = nombre,
                            pk = tablaResponse.pk,
                            queryCreacion = tablaResponse.queryCreacion,
                            batchSize = tablaResponse.batchSize,
                            filtro = tablaResponse.filtro,
                            error = tablaResponse.error,
                            numeroCampos = tablaResponse.numeroCampos,
                            metodoApp = tablaResponse.metodoApp,
                            fechaActualizacionSincro = tablaResponse.fechaActualizacionSincro
                        )
                    }
                }
                tablaDao.clearTablas()
                tablaDao.insertTablas(entities)
                Resource.Success(Unit)
            } else {
                Resource.Error("Error al obtener esquema: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión: ${e.message}")
        }
    }

    fun getTablasLocal(): Flow<List<Tabla>> =
        tablaDao.getTablas().map { list ->
            list.map {
                Tabla(
                    nombreTabla = it.nombreTabla,
                    pk = it.pk,
                    queryCreacion = it.queryCreacion,
                    batchSize = it.batchSize,
                    filtro = it.filtro,
                    error = it.error,
                    numeroCampos = it.numeroCampos,
                    metodoApp = it.metodoApp,
                    fechaActualizacionSincro = it.fechaActualizacionSincro
                )
            }
        }

    suspend fun getLocalidades(): Resource<List<Localidad>> {
        return try {
            val response = parametrosApi.obtenerLocalidades()
            if (response.isSuccessful) {
                val localidades = response.body()?.map {
                    Localidad(
                        idLocalidad = it.idLocalidad,
                        abreviacionCiudad = it.abreviacionCiudad,
                        nombreCompleto = it.nombreCompleto
                    )
                } ?: emptyList()
                Resource.Success(localidades)
            } else {
                Resource.Error("Error al obtener localidades: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión: ${e.message}")
        }
    }
}
