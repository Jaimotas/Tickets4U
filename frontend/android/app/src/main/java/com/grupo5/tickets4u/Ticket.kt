package com.grupo5.tickets4u

import com.google.gson.annotations.SerializedName

data class Ticket(
    val id: Long,
    val idCliente: Long,
    val idPedido: Long,
    // Si el wallet falla por el objeto Event, cambia temporalmente esto a Long
    // para ver si al menos aparecen los IDs en la lista.
    val evento: Event,
    val qr: String,
    val estado: String,
    val tipoEntrada: String
)