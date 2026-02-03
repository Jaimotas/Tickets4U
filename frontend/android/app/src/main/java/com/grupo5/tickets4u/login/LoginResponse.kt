package com.grupo5.tickets4u.login

import com.grupo5.tickets4u.Usuario

data class LoginResponse(
    val token: String,
    val usuario: Usuario
)
