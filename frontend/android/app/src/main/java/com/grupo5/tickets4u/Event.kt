package com.grupo5.tickets4u

import com.google.gson.annotations.SerializedName

data class Event(
    // id opcional para evitar errores con la API
    @SerializedName("id") val id: Long? = null,

    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fechaInicio") val fechaInicio: String,
    @SerializedName("fechaFin") val fechaFin: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("ubicacion") val ubicacion: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("aforo") val aforo: Int,
    @SerializedName("foto") val foto: String,
    @SerializedName("categoria") val categoria: String, // "ACTUAL", "DESTACADO" o "INTERNACIONAL"
    @SerializedName("idAdmin") val idAdmin: Int = 1,

    // NUEVOS CAMPOS: estadísticas que vienen en EventoDto
    @SerializedName("ticketsDisponibles") val ticketsDisponibles: Int? = null,
    @SerializedName("ticketsVendidos") val ticketsVendidos: Int? = null,
    @SerializedName("ingresos") val ingresos: Double? = null
)
