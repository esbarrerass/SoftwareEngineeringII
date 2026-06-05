package com.interrapidisimo.pruebaandroid.di

import com.interrapidisimo.pruebaandroid.data.remote.api.ParametrosApi
import com.interrapidisimo.pruebaandroid.data.remote.api.SeguridadApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL_PARAMETROS =
        "https://apitesting.interrapidisimo.co/apicontrollerpruebas/"

    private const val BASE_URL_SEGURIDAD =
        "https://apitesting.interrapidisimo.co/FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("parametros")
    fun provideParametrosRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL_PARAMETROS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("seguridad")
    fun provideSeguridadRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL_SEGURIDAD)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideParametrosApi(@Named("parametros") retrofit: Retrofit): ParametrosApi =
        retrofit.create(ParametrosApi::class.java)

    @Provides
    @Singleton
    fun provideSeguridadApi(@Named("seguridad") retrofit: Retrofit): SeguridadApi =
        retrofit.create(SeguridadApi::class.java)
}
