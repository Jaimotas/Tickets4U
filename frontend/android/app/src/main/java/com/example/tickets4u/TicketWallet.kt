package com.example.tickets4u

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime

class TicketWallet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_wallet)

        val rvTickets = findViewById<RecyclerView>(R.id.rvTickets)
        rvTickets.layoutManager = LinearLayoutManager(this)

        // Eventos de ejemplo
        val evento1 = Evento(
            id = 1,
            id_admin = 101,
            nombre = "Concierto Rock Night",
            descripcion = "Una noche inolvidable con las mejores bandas de rock.",
            fecha_inicio = LocalDateTime.of(2026,4,12,20,0),
            fecha_fin = LocalDateTime.of(2026,4,12,23,0),
            direccion = "Palacio de Deportes, Madrid",
            foto = "event_rocknight",
            qr = "qr_rocknight"
        )

        val evento2 = Evento(
            id = 2,
            id_admin = 102,
            nombre = "Mad Cool Festival",
            descripcion = "Festival internacional de música con los mejores artistas del año.",
            fecha_inicio = LocalDateTime.of(2026,7,10,12,0),
            fecha_fin = LocalDateTime.of(2026,7,12,23,0),
            direccion = "Parque Madrid Río, Madrid",
            foto = "event_madcool",
            qr = "qr_madcool"
        )

        // Tickets de ejemplo
        val tickets = listOf(
            Ticket(1, 1, "Concierto Rock Night", TicketStatus.VALIDO, "General"),
            Ticket(2, 2, "Mad Cool Festival", TicketStatus.VALIDO, "VIP")
        )

        // Crear mapa id_evento -> Evento
        val eventosMap = mapOf(
            evento1.id to evento1,
            evento2.id to evento2
        )

        rvTickets.adapter = TicketAdapter(this, tickets, eventosMap)
    }
}
