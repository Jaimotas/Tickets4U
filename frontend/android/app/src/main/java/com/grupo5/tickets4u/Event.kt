package com.grupo5.tickets4u

data class Event(
    val id: Int,
    val name: String,
    val location: String,
    val date: String,
    val imageResId: Int,
    val isTrending: Boolean = false   // NUEVO
)

