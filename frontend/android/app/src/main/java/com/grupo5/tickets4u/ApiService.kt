package com.grupo5.tickets4u

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// --- MODELOS DE DATOS ---

// Asegúrate de que esta clase coincida con tu Event.kt o defínela aquí
// Si ya tienes un archivo Event.kt en el mismo paquete, puedes borrar esta línea
// data class Event(val id: Long, val nombre: String, val descripcion: String, val imagenUrl: String, val precio: Double)

data class CrearTicketsRequest(
    val idCliente: Int,
    val idPedido: Long,
    val idEvento: Long,
    val tipoEntrada: String,
    val cantidad: Int
)

data class Ticket(
    val id: Long,
    val idCliente: Long,
    val idPedido: Long,
    val evento: Event, // <--- ESTO ARREGLA EL ERROR EN TICKETADAPTER
    val qr: String,
    val estado: String,
    val tipoEntrada: String
)

// --- INTERFAZ DE API ---

interface ApiService {

    @GET("eventos")
    suspend fun getEventos(): List<Event>

    @POST("eventos")
    suspend fun crearEvento(@Body evento: Event): Response<Event> // <--- ARREGLA CREAREVENTODIALOG

    @PUT("eventos/{id}")
    suspend fun editarEvento(@Path("id") id: Long, @Body evento: Event): Response<Event> // <--- ARREGLA CREAREVENTODIALOG

    @DELETE("eventos/{id}")
    suspend fun eliminarEvento(@Path("id") id: Long): Response<Unit> // <--- ARREGLA MAINACTIVITY

    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, Any>>

    @POST("tickets/crear-tickets")
    suspend fun crearTickets(@Body request: CrearTicketsRequest): Response<List<Ticket>>

    @GET("tickets/cliente/{idCliente}")
    suspend fun getTicketsCliente(@Path("idCliente") idCliente: Int): Response<List<Ticket>>
}

// --- CLIENTE RETROFIT ---

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