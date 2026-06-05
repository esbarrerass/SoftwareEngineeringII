package com.interrapidisimo.pruebaandroid.di

import android.content.Context
import androidx.room.Room
import com.interrapidisimo.pruebaandroid.data.local.AppDatabase
import com.interrapidisimo.pruebaandroid.data.local.dao.TablaDao
import com.interrapidisimo.pruebaandroid.data.local.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "prueba_tecnica_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideUsuarioDao(db: AppDatabase): UsuarioDao = db.usuarioDao()

    @Provides
    @Singleton
    fun provideTablaDao(db: AppDatabase): TablaDao = db.tablaDao()
}
