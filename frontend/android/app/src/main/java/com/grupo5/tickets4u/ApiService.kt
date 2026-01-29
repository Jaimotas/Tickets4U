package com.grupo5.tickets4u

import retrofit2.Response // Importante para que funcione Response<T>
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.* // Importa GET, POST, PUT, DELETE, Path, Body, etc.

interface ApiService {
    @GET("eventos")
    suspend fun getEventos(): List<Event>

    @POST("eventos")
    suspend fun crearEvento(@Body evento: Event): Response<Event>

    @PUT("eventos/{id}")
    suspend fun editarEvento(@Path("id") id: Long, @Body evento: Event): Response<Event>

    @DELETE("eventos/{id}")
    suspend fun eliminarEvento(@Path("id") id: Long): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}