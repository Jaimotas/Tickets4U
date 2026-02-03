package com.grupo5.tickets4u

import com.grupo5.tickets4u.login.LoginRequest
import com.grupo5.tickets4u.login.LoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.grupo5.tickets4u.login.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    @GET("eventos")
    suspend fun getEventos(): List<Event>

    @POST("eventos")
    suspend fun crearEvento(@Body evento: Event): retrofit2.Response<Event>

}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:9090/api/" // IP para emulador

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}