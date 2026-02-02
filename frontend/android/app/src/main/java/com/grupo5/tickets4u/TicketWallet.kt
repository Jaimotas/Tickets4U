package com.grupo5.tickets4u

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class TicketWallet : AppCompatActivity() {

    private lateinit var rvTickets: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_wallet)

        rvTickets = findViewById(R.id.rvTickets)
        rvTickets.layoutManager = LinearLayoutManager(this)

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