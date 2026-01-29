package com.grupo5.tickets4u

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets4u.Ticket
import com.example.tickets4u.TicketStatus

class TicketWallet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_wallet)

        val rvTickets = findViewById<RecyclerView>(R.id.rvTickets)
        rvTickets.layoutManager = LinearLayoutManager(this)

        // Eventos de ejemplo (usando Event real)
        val evento1 = Event(
            id = 1,
            idAdmin = 101,
            nombre = "Concierto Rock Night",
            descripcion = "Una noche inolvidable con las mejores bandas de rock.",
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
            id = 2,
            idAdmin = 102,
            nombre = "Mad Cool Festival",
            descripcion = "Festival internacional de música con los mejores artistas del año.",
            fechaInicio = "2026-07-10T12:00",
            fechaFin = "2026-07-12T23:00",
            ciudad = "Madrid",
            ubicacion = "Parque Madrid Río",
            direccion = "Parque Madrid Río, Madrid",
            aforo = 20000,
            foto = "event_madcool",
            categoria = "DESTACADO"
        )

        // Tickets de ejemplo
        val tickets = listOf(
            Ticket(1, 1, "Concierto Rock Night", TicketStatus.ACTIVO, "General"),
            Ticket(2, 2, "Mad Cool Festival", TicketStatus.ACTIVO, "VIP")
        )

        // Crear mapa id_evento -> Event
        val eventosMap = mapOf(
            evento1.id!! to evento1,
            evento2.id!! to evento2
        )

        rvTickets.adapter = TicketAdapter(this, tickets, eventosMap)
    }
}
