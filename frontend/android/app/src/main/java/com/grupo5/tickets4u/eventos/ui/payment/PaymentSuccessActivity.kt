package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.grupo5.tickets4u.MainActivity
import com.grupo5.tickets4u.R

class PaymentSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_payment_success)

        val txtMensaje = findViewById<TextView>(R.id.txtMensaje)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val totalPagado = intent.getDoubleExtra("TOTAL_PAGADO", 0.0)

        // Mostrar mensaje de éxito
        txtMensaje.text = "✅ ¡COMPRA EXITOSA!"
        txtTotal.text = "Has pagado: %.2f€".format(totalPagado)

        // Botón para volver al inicio
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
