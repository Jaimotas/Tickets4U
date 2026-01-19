package com.grupo5.tickets4u.model

data class TicketItem(
    val id: String,
    val nombreEvento: String,
    val precio: Double,
    var cantidad: Int
)
