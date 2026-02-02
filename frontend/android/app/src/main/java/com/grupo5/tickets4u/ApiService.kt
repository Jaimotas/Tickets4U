package com.grupo5.tickets4u

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// --- MODELOS DE DATOS ACTUALIZADOS ---

data class CrearTicketsRequest(
    val idCliente: Int,
    val idPedido: Long,
    val items: List<TicketItemRequest> // AHORA ES UNA LISTA
)

data class TicketItemRequest(
    val idEvento: Long,
    val tipoEntrada: String,
    val cantidad: Int
)

// --- INTERFAZ DE API ---

interface ApiService {

    @GET("eventos")
    suspend fun getEventos(): List<Event>

    @POST("eventos")
    suspend fun crearEvento(@Body evento: Event): Response<Event>

    @PUT("eventos/{id}")
    suspend fun editarEvento(@Path("id") id: Long, @Body evento: Event): Response<Event>

    @DELETE("eventos/{id}")
    suspend fun eliminarEvento(@Path("id") id: Long): Response<Unit>

    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, Any>>

    // SECCIÓN DE TICKETS: Recibe el nuevo objeto con la lista
    @POST("tickets/crear-tickets")
    suspend fun crearTickets(@Body request: CrearTicketsRequest): Response<List<Ticket>>

    @GET("tickets/cliente/{idCliente}")
    suspend fun getTicketsCliente(@Path("idCliente") idCliente: Int): Response<List<Ticket>>

    @GET("tickets/qr/{qr}")
    suspend fun validarTicket(@Path("qr") qr: String): Response<Ticket>
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