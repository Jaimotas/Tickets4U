package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.grupo5.tickets4u.* // Importa ApiService, RetrofitClient y las Data Classes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentLoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_payment_loading)

        val total = intent.getDoubleExtra("TOTAL_PAGADO", 0.0)
        val idEvento = intent.getLongExtra("ID_EVENTO", 1L)
        val cantidad = intent.getIntExtra("CANTIDAD", 1)

        // ¡OJO! Asegúrate de que el ID 1 exista en tu tabla de usuarios en MySQL
        val idCliente = 1

        procesarCompra(idCliente, idEvento, cantidad, total)
    }

    private fun procesarCompra(idCliente: Int, idEvento: Long, cantidad: Int, total: Double) {
        lifecycleScope.launch {
            try {
                delay(1500)

                // PASO 1: Crear el Pedido enviando el objeto Evento anidado
                val pedidoData = mapOf(
                    "idCliente" to idCliente,
                    "total" to total,
                    "pago" to "Tarjeta",
                    "evento" to mapOf("id" to idEvento)
                )

                val responsePedido = RetrofitClient.instance.crearPedido(pedidoData)

                if (responsePedido.isSuccessful && responsePedido.body() != null) {
                    val body = responsePedido.body()!!

                    // El ID suele venir como Double desde el JSON de Gson
                    val idPedidoGenerado = (body["id"] as? Double)?.toLong()
                        ?: (body["id"] as? Int)?.toLong()
                        ?: throw Exception("ID de pedido no recibido")

                    Log.d("API_SUCCESS", "Pedido creado con éxito. ID: $idPedidoGenerado")

                    // PASO 2: Crear los Tickets asociados al ID obtenido
                    val requestTickets = CrearTicketsRequest(
                        idCliente = idCliente,
                        idPedido = idPedidoGenerado,
                        idEvento = idEvento,
                        tipoEntrada = "General",
                        cantidad = cantidad
                    )

                    val responseTickets = RetrofitClient.instance.crearTickets(requestTickets)

                    if (responseTickets.isSuccessful) {
                        val intent = Intent(this@PaymentLoadingActivity, PaymentSuccessActivity::class.java)
                        intent.putExtra("TOTAL_PAGADO", total)
                        startActivity(intent)
                        finish()
                    } else {
                        manejarError("Error en Tickets: ${responseTickets.code()}")
                    }
                } else {
                    // Si llega aquí, es posible que el servidor devolviera 400, 404 o 500
                    Log.e("API_ERROR", "Error en Pedido: ${responsePedido.errorBody()?.string()}")
                    manejarError("El servidor rechazó el pedido (${responsePedido.code()})")
                }

            } catch (e: Exception) {
                Log.e("API_ERROR", "Excepción: ${e.message}")
                manejarError("Sin respuesta del servidor. Verifica que Spring Boot esté corriendo.")
            }
        }
    }

    private fun manejarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        finish()
    }
}