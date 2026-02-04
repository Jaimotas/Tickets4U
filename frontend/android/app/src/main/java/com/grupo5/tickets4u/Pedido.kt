package com.grupo5.tickets4u

data class Pedido(
    val id: Long? = null, // El servidor lo genera y lo devuelve aquí
    val idCliente: Int,
    val idEvento: Long,
    val total: Double,
    val pago: String
)