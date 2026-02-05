package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.grupo5.tickets4u.ApiService
import com.grupo5.tickets4u.R
import com.grupo5.tickets4u.SessionManager
import com.grupo5.tickets4u.eventos.ui.cart.CartManager
import kotlinx.coroutines.launch

class PaymentLoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_payment_loading)

        val total = intent.getDoubleExtra("TOTAL_PAGADO", 0.0)
        val idEvento = intent.getLongExtra("ID_EVENTO", -1L)

        if (idEvento == -1L) {
            manejarError("No se recibió el ID del evento")
            return
        }

        val idCliente = SessionManager.getUserId(this)
        if (idCliente == -1L) {
            manejarError("No se encontró usuario logueado")
            return
        }

        // Llamada al backend solo una vez
        procesarCompraCompleta(total, idEvento, idCliente)
    }

    private fun procesarCompraCompleta(total: Double, idEvento: Long, idCliente: Long) {
        lifecycleScope.launch {
            try {
                // POST al backend
                ApiService.RetrofitClient.instance.confirmarPedido(total, idEvento)

                // Si todo OK
                CartManager.clearCart()

                val intentExito = Intent(this@PaymentLoadingActivity, PaymentSuccessActivity::class.java)
                intentExito.putExtra("TOTAL_PAGADO", total)
                startActivity(intentExito)
                finish()

            } catch (e: Exception) {
                manejarError("Error al confirmar el pedido: ${e.message}")
            }
        }
    }

    private fun manejarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        finish()
    }
}
