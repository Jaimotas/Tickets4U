package com.grupo5.tickets4u

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("fechaInicio") val fechaInicio: String? = null,
    @SerializedName("fechaFin") val fechaFin: String? = null,
    @SerializedName("ciudad") val ciudad: String? = null,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("aforo") val aforo: Int? = null, // Restaurado para CrearEventoDialogFragment
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("categoria") val categoria: String? = null,
    @SerializedName("idAdmin") val idAdmin: Int? = 1, // Restaurado para evitar errores de parámetro

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
    @SerializedName("idAdmin")  val idAdmin: Usuario?
    // Campos de estadísticas restaurados para EventAdapter
    @SerializedName("ticketsDisponibles") val ticketsDisponibles: Int? = null,
    @SerializedName("ticketsVendidos") val ticketsVendidos: Int? = null,
    @SerializedName("ingresos") val ingresos: Double? = null
)
