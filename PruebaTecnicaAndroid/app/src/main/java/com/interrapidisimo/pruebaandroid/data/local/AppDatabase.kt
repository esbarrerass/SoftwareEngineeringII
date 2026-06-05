package com.interrapidisimo.pruebaandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.interrapidisimo.pruebaandroid.data.local.dao.TablaDao
import com.interrapidisimo.pruebaandroid.data.local.dao.UsuarioDao
import com.interrapidisimo.pruebaandroid.data.local.entity.TablaEntity
import com.interrapidisimo.pruebaandroid.data.local.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, TablaEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun tablaDao(): TablaDao
}
