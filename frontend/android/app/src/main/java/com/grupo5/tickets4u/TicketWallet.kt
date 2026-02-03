package com.grupo5.tickets4u

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// Asegúrate de que estas rutas sean las correctas en tu proyecto
import com.example.tickets4u.Ticket
import com.example.tickets4u.TicketStatus
import kotlinx.coroutines.launch

class TicketWallet : AppCompatActivity() {

    private lateinit var rvTickets: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_wallet)

        rvTickets = findViewById(R.id.rvTickets)
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
        // Asegúrate de que este ID existe en tu base de datos de Spring Boot
        val idClienteLogueado = 1
        cargarTicketsDesdeBackend(idClienteLogueado)
    }

    private fun cargarTicketsDesdeBackend(idCliente: Int) {
        lifecycleScope.launch {
            try {
                // 1. Obtenemos el objeto Response completo
                val response = RetrofitClient.instance.getTicketsCliente(idCliente)

                // 2. Comprobamos si el servidor respondió con éxito (Código 200)
                if (response.isSuccessful) {
                    // 3. Extraemos la lista del cuerpo de la respuesta
                    val listaTickets = response.body() ?: emptyList()

                    if (listaTickets.isNotEmpty()) {
                        // Ahora sí, pasamos la List<Ticket> al adaptador
                        rvTickets.adapter = TicketAdapter(this@TicketWallet, listaTickets)
                    } else {
                        Toast.makeText(this@TicketWallet, "No tienes tickets comprados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si el servidor responde un error (ej. 404 o 500)
                    Log.e("API_ERROR", "Código de error: ${response.code()}")
                    Toast.makeText(this@TicketWallet, "Error del servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                // Si ni siquiera se pudo conectar (IP mal, servidor apagado)
                Log.e("API_ERROR", "Fallo de red: ${e.message}")
                Toast.makeText(this@TicketWallet, "Error de conexión: Revisa el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }
}