package com.grupo5.tickets4u

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// --- MODELOS DE DATOS ---
// Nota: Event y Ticket deben estar definidos en sus propios archivos
// dentro del paquete com.grupo5.tickets4u para evitar errores de redodeclaración.

data class CrearTicketsRequest(
    val idCliente: Int,
    val idPedido: Long,
    val idEvento: Long,
    val tipoEntrada: String,
    val cantidad: Int
)

// --- INTERFAZ DE API ---

interface ApiService {

    // SECCIÓN DE EVENTOS
    @GET("eventos")
    suspend fun getEventos(): List<Event>

    @POST("eventos")
    suspend fun crearEvento(@Body evento: Event): Response<Event>

    @PUT("eventos/{id}")
    suspend fun editarEvento(@Path("id") id: Long, @Body evento: Event): Response<Event>

    @DELETE("eventos/{id}")
    suspend fun eliminarEvento(@Path("id") id: Long): Response<Unit>

    // SECCIÓN DE COMPRAS (PEDIDOS)
    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, Any>>

    // SECCIÓN DE TICKETS
    @POST("tickets/crear-tickets")
    suspend fun crearTickets(@Body request: CrearTicketsRequest): Response<List<Ticket>>

    @GET("tickets/cliente/{idCliente}")
    suspend fun getTicketsCliente(@Path("idCliente") idCliente: Int): Response<List<Ticket>>

    @GET("tickets/qr/{qr}")
    suspend fun validarTicket(@Path("qr") qr: String): Response<Ticket>
}

// --- CLIENTE RETROFIT ---

object RetrofitClient {
    // 10.0.2.2 es el host local para el emulador de Android
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}