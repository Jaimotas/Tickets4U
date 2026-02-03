package com.grupo5.tickets4u

import retrofit2.Response
import retrofit2.http.*

data class QrResponse(val status: String, val message: String? = null)

data class CrearTicketsRequest(
    val idCliente: Int,
    val idPedido: Long,
    val items: List<TicketItemRequest>
)

data class TicketItemRequest(
    val idEvento: Long,
    val tipoEntrada: String,
    val cantidad: Int
)

interface ApiService {
    // EVENTOS
    @GET("eventos")
    suspend fun getEventos(): List<Event>

    @POST("eventos")
    suspend fun crearEvento(@Body evento: Event): Response<Event>

    @PUT("eventos/{id}")
    suspend fun editarEvento(@Path("id") id: Long, @Body evento: Event): Response<Event>

    @DELETE("eventos/{id}")
    suspend fun eliminarEvento(@Path("id") id: Long): Response<Unit>

    // COMPRAS Y TICKETS
    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, Any>>

    @POST("tickets/crear-tickets")
    suspend fun crearTickets(@Body request: CrearTicketsRequest): Response<List<Ticket>>

    @GET("tickets/cliente/{idCliente}")
    suspend fun getTicketsCliente(@Path("idCliente") idCliente: Int): Response<List<Ticket>>

    // VALIDACIÓN QR
    @GET("tickets/validate")
    suspend fun validarQr(@Query("qr") qrCode: String): QrResponse
}