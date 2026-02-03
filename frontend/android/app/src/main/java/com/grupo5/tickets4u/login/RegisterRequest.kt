package com.grupo5.tickets4u.login

data class RegisterRequest(
    val nombreUsuario: String,
    val email: String,
    val contrasena: String
)