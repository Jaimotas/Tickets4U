package com.grupo5.tickets4u

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("eventos")
    suspend fun getEventos(): List<Event>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/" // IP para emulador

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}