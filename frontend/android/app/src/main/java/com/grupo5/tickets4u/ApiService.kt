package com.grupo5.tickets4u

import retrofit2.Response // Importante para que funcione Response<T>
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// ← NUEVO: Data class para respuesta QR
data class QrResponse(
    val status: String,
    val message: String? = null
)

interface ApiService {
    @GET("eventos")
    suspend fun getEventos(): List<Event>

    @POST("eventos")
    suspend fun crearEvento(@Body evento: Event): retrofit2.Response<Event>

    // ← NUEVO: Validación QR (tu backend puerto 9090)
    @GET("tickets/validate")
    suspend fun validarQr(@Query("qr") qrCode: String): QrResponse
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
