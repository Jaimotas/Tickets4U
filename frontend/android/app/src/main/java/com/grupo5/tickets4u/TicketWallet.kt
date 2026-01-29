package com.grupo5.tickets4u

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// Asegúrate de que estas rutas sean las correctas en tu proyecto
import com.example.tickets4u.Ticket
import com.example.tickets4u.TicketStatus

class TicketWallet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_wallet)

        val rvTickets = findViewById<RecyclerView>(R.id.rvTickets)
        rvTickets.layoutManager = LinearLayoutManager(this)

        val evento1 = Event(
            id = 1L, // Usamos L para indicar que es Long si tu clase Event lo requiere
            idAdmin = 101,
            nombre = "Concierto Rock Night",
            descripcion = "Una noche inolvidable...",
            fechaInicio = "2026-04-12T20:00",
            fechaFin = "2026-04-12T23:00",
            ciudad = "Madrid",
            ubicacion = "Palacio de Deportes",
            direccion = "Palacio de Deportes, Madrid",
            aforo = 5000,
            foto = "event_rocknight",
            categoria = "ACTUAL"
        )

        val evento2 = Event(
            id = 2L,
            idAdmin = 102,
            nombre = "Mad Cool Festival",
            descripcion = "Festival internacional...",
            fechaInicio = "2026-07-10T12:00",
            fechaFin = "2026-07-12T23:00",
            ciudad = "Madrid",
            ubicacion = "Parque Madrid Río",
            direccion = "Parque Madrid Río, Madrid",
            aforo = 20000,
            foto = "event_madcool",
            categoria = "DESTACADO"
        )

        val tickets = listOf(
            Ticket(1, 1, "Concierto Rock Night", TicketStatus.ACTIVO, "General"),
            Ticket(2, 2, "Mad Cool Festival", TicketStatus.ACTIVO, "VIP")
        )

        // Convertimos el ID a Int explícitamente para que coincida con lo que espera el TicketAdapter
        val eventosMap: Map<Int, Event> = mapOf(
            evento1.id!!.toInt() to evento1,
            evento2.id!!.toInt() to evento2
        )

        rvTickets.adapter = TicketAdapter(this, tickets, eventosMap)
    }
}