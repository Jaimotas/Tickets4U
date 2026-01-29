package com.grupo5.tickets4u

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TicketDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        val tvName = findViewById<TextView>(R.id.tvEventName)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvDates = findViewById<TextView>(R.id.tvDates)
        val tvAddress = findViewById<TextView>(R.id.tvAddress)
        val tvType = findViewById<TextView>(R.id.tvType)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val imgEvent = findViewById<ImageView>(R.id.imgEvent)
        val imgQr = findViewById<ImageView>(R.id.imgQr)

        val nombre = intent.getStringExtra("nombre") ?: "Sin nombre"
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val fechaInicio = intent.getStringExtra("fecha_inicio") ?: ""
        val fechaFin = intent.getStringExtra("fecha_fin") ?: ""
        val direccion = intent.getStringExtra("direccion") ?: ""
        val tipoEntrada = intent.getStringExtra("tipo_entrada") ?: ""
        val estado = intent.getStringExtra("estado") ?: ""
        val foto = intent.getStringExtra("foto") ?: ""
        // qr eliminado porque tu Event no tiene
        // val qr = intent.getStringExtra("qr") ?: ""

        tvName.text = nombre
        tvDescription.text = descripcion
        tvDates.text = "📅 Del $fechaInicio al $fechaFin"
        tvAddress.text = "📍 $direccion"
        tvType.text = "Tipo: $tipoEntrada"
        tvStatus.text = "Estado: $estado"

        if (foto.isNotEmpty()) {
            val imgResId = resources.getIdentifier(foto, "drawable", packageName)
            if (imgResId != 0) imgEvent.setImageResource(imgResId)
        }

        // QR eliminado, si quieres mostrar QR tendrás que usar Glide o crear un drawable de prueba
        // val qrResId = resources.getIdentifier(qr, "drawable", packageName)
        // if (qrResId != 0) imgQr.setImageResource(qrResId)
    }
}
