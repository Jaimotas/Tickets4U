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

        // Mantenemos el ID como Long para la lógica general
        val idClienteFijo = 1L

        procesarCompraCompleta(idClienteFijo, total)
    }

    private fun procesarCompraCompleta(idCliente: Long, total: Double) {
        lifecycleScope.launch {
            try {
                delay(1500)

                val itemsDelCarrito = CartManager.getItems()

                if (itemsDelCarrito.isEmpty()) {
                    manejarError("El carrito está vacío")
                    return@launch
                }

                val primerItem = itemsDelCarrito[0]

                // 1. CREAR ESTRUCTURA PARA EL PEDIDO
                val pedidoData = HashMap<String, Any>()
                val clienteMap = HashMap<String, Any>()
                clienteMap["id"] = idCliente
                pedidoData["cliente"] = clienteMap

                pedidoData["total"] = total
                pedidoData["pago"] = "Tarjeta"

                val eventoMap = HashMap<String, Any>()
                eventoMap["id"] = primerItem.id.toLong()
                pedidoData["evento"] = eventoMap

                val responsePedido = RetrofitClient.instance.crearPedido(pedidoData)

                if (responsePedido.isSuccessful && responsePedido.body() != null) {
                    val body = responsePedido.body()!!
                    val idPedidoGenerado = (body["id"]?.toString()?.toDouble() ?: 0.0).toLong()

                    // 2. CREAR TICKETS PARA CADA ITEM DEL CARRITO
                    val listaTicketsRequest = mutableListOf<TicketItemRequest>()

                    for (item in itemsDelCarrito) {
                        val ticketReq = TicketItemRequest(
                            idEvento = item.id.toLong(),
                            tipoEntrada = "General",
                            cantidad = item.cantidad
                        )
                        listaTicketsRequest.add(ticketReq)
                    }

                    // CORRECCIÓN AQUÍ: Convertimos idCliente de Long a Int
                    val requestTickets = CrearTicketsRequest(
                        idCliente = idCliente.toInt(), // <--- SOLUCIÓN AL TYPE MISMATCH
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
                        manejarError("Error al generar tickets: ${responseTickets.code()}")
                    }
                } else {
                    manejarError("Error en el servidor al crear el pedido")
                }

            } catch (e: Exception) {
                Log.e("API_ERROR", "Fallo crítico: ${e.message}")
                manejarError("Error de conexión con el servidor")
            }
        }
    }

    private fun manejarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        finish()
    }
}