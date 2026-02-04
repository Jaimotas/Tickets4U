package com.grupo5.tickets4u

import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("id") val id: Long,
    @SerializedName("evento") val evento: Event?, // Objeto Evento completo
    @SerializedName("qr") val qr: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("tipo_entrada") val tipoEntrada: String?
)