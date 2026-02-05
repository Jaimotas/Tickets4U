package com.grupo5.tickets4u

import android.content.Intent
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

        val userId = SessionManager.getUserId(this)
        if (userId == -1L) {
            Toast.makeText(this, "No estás logueado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarTicketsDesdeBackend(userId)
    }


    private fun cargarTicketsDesdeBackend(idCliente: Long) {
        lifecycleScope.launch {
            try {
                val response = ApiService.RetrofitClient.instance.getTicketsCliente(idCliente)

                if (response.isSuccessful) {
                    val listaTickets = response.body() ?: emptyList()

                    if (listaTickets.isNotEmpty()) {
                        rvTickets.adapter = TicketAdapter(this@TicketWallet, listaTickets) { ticket ->
                            // CREACIÓN DEL INTENT PARA IR AL DETALLE
                            val intent = Intent(this@TicketWallet, TicketDetailActivity::class.java).apply {
                                // Datos del evento (navegamos por la relación)
                                putExtra("nombre", ticket.evento?.nombre)
                                putExtra("descripcion", ticket.evento?.descripcion)
                                putExtra("fecha_inicio", ticket.evento?.fechaInicio)
                                putExtra("fecha_fin", ticket.evento?.fechaFin)
                                putExtra("direccion", ticket.evento?.direccion)
                                putExtra("foto", ticket.evento?.foto)

                                // Datos del ticket
                                putExtra("tipo_entrada", ticket.tipoEntrada)
                                putExtra("estado", ticket.estado)
                                putExtra("qr_string", ticket.qr)
                            }
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@TicketWallet, "No tienes tickets", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Fallo: ${e.message}")
                Toast.makeText(this@TicketWallet, "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }
}