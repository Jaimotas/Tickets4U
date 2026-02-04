package com.grupo5.tickets4u

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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

        val nombre = intent.getStringExtra("nombre") ?: "Evento desconocido"
        val descripcion = intent.getStringExtra("descripcion") ?: "Sin descripción disponible"
        val fechaInicioRaw = intent.getStringExtra("fecha_inicio") ?: ""
        val fechaFinRaw = intent.getStringExtra("fecha_fin") ?: ""
        val direccion = intent.getStringExtra("direccion") ?: "Ubicación no especificada"
        val tipoEntrada = intent.getStringExtra("tipo_entrada") ?: "General"
        val estado = intent.getStringExtra("estado") ?: "activo"
        val foto = intent.getStringExtra("foto") ?: ""
        val qrString = intent.getStringExtra("qr_string") ?: ""

        // FORMATEO DE FECHAS
        val fechaInicioFormateada = formatearFecha(fechaInicioRaw)
        val fechaFinFormateada = formatearFecha(fechaFinRaw)

        tvName.text = nombre
        tvDescription.text = descripcion
        tvDates.text = "📅 Inicio: $fechaInicioFormateada\n📅 Fin: $fechaFinFormateada"
        tvAddress.text = "📍 $direccion"
        tvType.text = "Entrada: $tipoEntrada"

        tvStatus.text = "Estado: ${estado.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"

        if (foto.isNotEmpty()) {
            val imgResId = resources.getIdentifier(foto, "drawable", packageName)
            if (imgResId != 0) {
                imgEvent.setImageResource(imgResId)
            } else {
                imgEvent.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        imgQr.setImageResource(android.R.drawable.ic_menu_camera)
    }

    /**
     * Convierte un String de fecha del backend al formato DD/MM/YYYY
     */
    private fun formatearFecha(fechaRaw: String): String {
        return try {
            // El backend suele enviar: 2024-02-23T10:00:00 o 2024-02-23
            // Tomamos solo los primeros 10 caracteres (YYYY-MM-DD)
            val soloFecha = fechaRaw.take(10)
            val parts = soloFecha.split("-")
            if (parts.size == 3) {
                "${parts[2]}/${parts[1]}/${parts[0]}" // Retorna DD/MM/YYYY
            } else {
                fechaRaw
            }
        } catch (e: Exception) {
            fechaRaw // Si falla, devuelve la original para no romper la app
        }
    }
}