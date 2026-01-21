package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.grupo5.tickets4u.R

class PaymentLoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_payment_loading)

        val total = intent.getDoubleExtra("TOTAL_PAGADO", 0.0)

        // Simular tiempo de procesamiento (3-5 segundos)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // Ir a pantalla de éxito
            val intent = Intent(this, PaymentSuccessActivity::class.java)
            intent.putExtra("TOTAL_PAGADO", total)
            startActivity(intent)
            finish()
        }, 3500) // 3.5 segundos de espera
    }
}