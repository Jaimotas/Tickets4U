package com.grupo5.tickets4u

import com.google.gson.annotations.SerializedName

data class Event(
    val id: Int? = null,
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
)
