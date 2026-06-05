package com.interrapidisimo.pruebaandroid.data.repository

import com.interrapidisimo.pruebaandroid.data.local.dao.UsuarioDao
import com.interrapidisimo.pruebaandroid.data.local.entity.UsuarioEntity
import com.interrapidisimo.pruebaandroid.data.remote.api.ParametrosApi
import com.interrapidisimo.pruebaandroid.data.remote.api.SeguridadApi
import com.interrapidisimo.pruebaandroid.data.remote.model.AuthRequest
import com.interrapidisimo.pruebaandroid.domain.model.Usuario
import com.interrapidisimo.pruebaandroid.util.AppConfig
import com.interrapidisimo.pruebaandroid.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeguridadRepository @Inject constructor(
    private val seguridadApi: SeguridadApi,
    private val parametrosApi: ParametrosApi,
    private val usuarioDao: UsuarioDao
) {

    // Local version embedded in this build
    private val localVersion = "1.0"

    /**
     * Compares local version against the remote version from the API.
     * Returns a descriptive message if versions differ, null if equal.
     */
    suspend fun checkVersion(): Resource<String?> {
        return try {
            val response = parametrosApi.consultarParametro("VPStoreAppControl")
            if (response.isSuccessful) {
                val remoteVersion = response.body()?.trim()
                when {
                    remoteVersion == null -> Resource.Error("No se pudo obtener la versión remota")
                    isVersionLower(localVersion, remoteVersion) ->
                        Resource.Success("La versión local ($localVersion) es inferior a la versión en API ($remoteVersion). Actualice la aplicación.")
                    isVersionHigher(localVersion, remoteVersion) ->
                        Resource.Success("La versión local ($localVersion) es superior a la versión en API ($remoteVersion).")
                    else -> Resource.Success(null)
                }
            } else {
                Resource.Error("Error al consultar versión: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión: ${e.message}")
        }
    }

    suspend fun login(usuario: String, identificacion: String): Resource<Usuario> {
        return try {
            val response = seguridadApi.authenticaUsuario(
                usuario = usuario,
                identificacion = identificacion,
                accept = "text/json",
                idUsuario = usuario,
                idCentroServicio = AppConfig.ID_CENTRO_SERVICIO,
                nombreCentroServicio = AppConfig.NOMBRE_CENTRO_SERVICIO,
                idAplicativoOrigen = AppConfig.ID_APLICATIVO_ORIGEN,
                body = AuthRequest()
            )
            if (response.isSuccessful) {
                val auth = response.body()
                if (auth != null && auth.usuario != null) {
                    val usuario = Usuario(
                        usuario = auth.usuario,
                        identificacion = auth.identificacion,
                        nombre = auth.nombre
                    )
                    usuarioDao.clearUsuarios()
                    usuarioDao.insertUsuario(
                        UsuarioEntity(
                            usuario = usuario.usuario,
                            identificacion = usuario.identificacion,
                            nombre = usuario.nombre
                        )
                    )
                    Resource.Success(usuario)
                } else {
                    Resource.Error("Autenticación fallida. Verifique las credenciales.")
                }
            } else {
                Resource.Error("Error de autenticación: código ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión: ${e.message}")
        }
    }

    fun getUsuarioLocal(): Flow<Usuario?> =
        usuarioDao.getUsuario().map { entity ->
            entity?.let { Usuario(it.usuario, it.identificacion, it.nombre) }
        }

    private fun isVersionLower(local: String, remote: String): Boolean {
        val localParts = local.split(".").mapNotNull { it.toIntOrNull() }
        val remoteParts = remote.split(".").mapNotNull { it.toIntOrNull() }
        for (i in 0 until maxOf(localParts.size, remoteParts.size)) {
            val l = localParts.getOrElse(i) { 0 }
            val r = remoteParts.getOrElse(i) { 0 }
            if (l < r) return true
            if (l > r) return false
        }
        return false
    }

    private fun isVersionHigher(local: String, remote: String): Boolean {
        val localParts = local.split(".").mapNotNull { it.toIntOrNull() }
        val remoteParts = remote.split(".").mapNotNull { it.toIntOrNull() }
        for (i in 0 until maxOf(localParts.size, remoteParts.size)) {
            val l = localParts.getOrElse(i) { 0 }
            val r = remoteParts.getOrElse(i) { 0 }
            if (l > r) return true
            if (l < r) return false
        }
        return false
    }
}
