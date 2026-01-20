package com.grupo5.tickets4u

import com.google.gson.annotations.SerializedName

data class Event(
    val id: Int,
    @SerializedName("nombre") val name: String,
    @SerializedName("ubicacion") val location: String,
    @SerializedName("ciudad") val city: String,
    @SerializedName("fechaInicio") val date: String,
    @SerializedName("foto") val imageUrl: String
)