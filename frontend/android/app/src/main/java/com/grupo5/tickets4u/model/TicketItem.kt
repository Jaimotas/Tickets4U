package com.grupo5.tickets4u.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TicketItem(
    val id: String,
    val nombreEvento: String,
    val precio: Double,
    var cantidad: Int,
    val imagenUrl: String? = null
    val eventoId: Long
) : Parcelable
