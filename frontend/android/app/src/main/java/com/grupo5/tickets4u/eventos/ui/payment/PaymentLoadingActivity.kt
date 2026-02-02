package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.grupo5.tickets4u.*
import com.grupo5.tickets4u.eventos.ui.cart.CartManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentLoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_payment_loading)

        val total = intent.getDoubleExtra("TOTAL_PAGADO", 0.0)
        val idCliente = 1

        procesarCompraCompleta(idCliente, total)
    }

    private fun procesarCompraCompleta(idCliente: Int, total: Double) {
        lifecycleScope.launch {
            try {
                delay(1500)

                // 1. Usamos getItems() de tu CartManager
                val itemsDelCarrito = CartManager.getItems()

                if (itemsDelCarrito.isEmpty()) {
                    manejarError("El carrito está vacío")
                    return@launch
                }

                val primerItem = itemsDelCarrito[0]

                // 2. Crear el Pedido
                val pedidoData = HashMap<String, Any>()
                pedidoData["idCliente"] = idCliente
                pedidoData["total"] = total
                pedidoData["pago"] = "Tarjeta"

                val eventoData = HashMap<String, Any>()
                // Convertimos el String id a Long para el backend
                eventoData["id"] = primerItem.id.toLong()
                pedidoData["evento"] = eventoData

                val responsePedido = RetrofitClient.instance.crearPedido(pedidoData)

                if (responsePedido.isSuccessful && responsePedido.body() != null) {
                    val body = responsePedido.body()!!
                    val idPedidoGenerado = (body["id"]?.toString()?.toDouble() ?: 0.0).toLong()

                    // 3. Transformar items usando TUS nombres de variables: id y cantidad
                    val listaTicketsRequest = mutableListOf<TicketItemRequest>()

                    for (item in itemsDelCarrito) {
                        val ticketReq = TicketItemRequest(
                            idEvento = item.id.toLong(), // Usamos tu 'id' convertido
                            tipoEntrada = "General",     // Valor por defecto ya que no está en TicketItem
                            cantidad = item.cantidad      // Usamos tu 'cantidad'
                        )
                        listaTicketsRequest.add(ticketReq)
                    }

                    val requestTickets = CrearTicketsRequest(
                        idCliente = idCliente,
                        idPedido = idPedidoGenerado,
                        items = listaTicketsRequest
                    )

                    val responseTickets = RetrofitClient.instance.crearTickets(requestTickets)

                    if (responseTickets.isSuccessful) {
                        CartManager.clearCart()
                        val intentExito = Intent(this@PaymentLoadingActivity, PaymentSuccessActivity::class.java)
                        intentExito.putExtra("TOTAL_PAGADO", total)
                        startActivity(intentExito)
                        finish()
                    } else {
                        manejarError("Error Tickets: ${responseTickets.code()}")
                    }
                } else {
                    manejarError("Error Pedido: ${responsePedido.code()}")
                }

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                manejarError("Datos inválidos: Asegúrate de que el ID sea numérico")
            }
        }
    }

    private fun manejarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        finish()
    }
}