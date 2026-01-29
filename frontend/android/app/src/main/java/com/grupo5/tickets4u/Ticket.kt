package com.example.tickets4u

data class Ticket(
    val id: Int,
    val id_evento: Int,
    val nombre: String,
    val estado: TicketStatus, // <-- aquí usamos el enum
    val tipo_entrada: String
)
enum class TicketStatus {
    ACTIVO,
    USADO,
    CANCELADO
}
