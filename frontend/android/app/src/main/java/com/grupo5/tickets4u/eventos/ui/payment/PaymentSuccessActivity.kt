package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.grupo5.tickets4u.MainActivity
import com.grupo5.tickets4u.R
import com.grupo5.tickets4u.eventos.ui.cart.CartManager // Importamos el manager

class PaymentSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_payment_success)

        // 1. LIMPIAR EL CARRITO INMEDIATAMENTE
        // Al ser un Singleton, esto vacía la lista global para la próxima compra
        CartManager.clearCart()

        // 2. Referencias de la UI
        val txtMensaje = findViewById<TextView>(R.id.txtMensaje)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // 3. Obtener el total del intent
        val totalPagado = intent.getDoubleExtra("TOTAL_PAGADO", 0.0)

        // 4. Mostrar mensaje de éxito
        txtMensaje.text = "✅ ¡COMPRA EXITOSA!"
        txtTotal.text = "Has pagado: %.2f€".format(totalPagado)

        // 5. Botón para volver al inicio
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // Usamos flags para que no pueda volver atrás a la pantalla de éxito
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}