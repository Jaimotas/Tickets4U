package com.grupo5.tickets4u
enum class Rol {
    admin,
    cliente
}

data class Usuario(
    val id: Long,
    val nombreUsuario: String,
    val email: String,
    val contrasena: String,
    val rol: Rol
)
