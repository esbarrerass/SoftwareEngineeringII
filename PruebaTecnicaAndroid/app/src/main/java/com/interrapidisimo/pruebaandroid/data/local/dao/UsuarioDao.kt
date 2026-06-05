package com.interrapidisimo.pruebaandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.interrapidisimo.pruebaandroid.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuario LIMIT 1")
    fun getUsuario(): Flow<UsuarioEntity?>

    @Query("DELETE FROM usuario")
    suspend fun clearUsuarios()
}
