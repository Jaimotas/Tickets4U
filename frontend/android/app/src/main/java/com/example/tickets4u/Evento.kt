package com.example.tickets4u

import java.time.LocalDateTime

data class Evento(
    val id: Int,
    val id_admin: Int,
    val nombre: String,
    val descripcion: String,
    val fecha_inicio: LocalDateTime,
    val fecha_fin: LocalDateTime,
    val direccion: String,
    val foto: String,   // imagen del evento
    val qr: String? = null // QR opcional
)
