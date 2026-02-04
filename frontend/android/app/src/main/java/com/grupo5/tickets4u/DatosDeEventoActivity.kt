package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class DatosDeEventoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos_de_evento)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Referencias
        val ivEventImage = findViewById<ImageView>(R.id.ivEventImage)
        val tvTitle = findViewById<TextView>(R.id.tvEventTitle)
        val tvDate = findViewById<TextView>(R.id.tvEventDate)
        val tvLocation = findViewById<TextView>(R.id.tvEventLocation)
        val tvDescription = findViewById<TextView>(R.id.tvEventDescription)
        val btnComprar = findViewById<ExtendedFloatingActionButton>(R.id.fabBuyTickets)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // 2. Recibir datos
        val eventoId = intent.getLongExtra("EVENTO_ID", -1)
        val nombre = intent.getStringExtra("EVENTO_NOMBRE")
        val fechaRaw = intent.getStringExtra("EVENTO_FECHA") ?: ""
        val ubicacion = intent.getStringExtra("EVENTO_UBICACION")
        val fotoUrl = intent.getStringExtra("EVENTO_FOTO")
        val descripcion = intent.getStringExtra("EVENTO_DESCRIPCION") ?: "Sin descripción."

        // 3. Formatear Fecha (ISO 2026-01-27T20:00:00 -> 2026-01-27 · 20:00)
        val fechaLimpia = try {
            if (fechaRaw.contains("T")) {
                val partes = fechaRaw.split("T")
                val soloFecha = partes[0]
                val soloHora = partes[1].substring(0, 5)
                "$soloFecha · $soloHora"
            } else fechaRaw
        } catch (e: Exception) { fechaRaw }

        // 4. Asignar datos
        tvTitle.text = nombre
        tvDate.text = fechaLimpia
        tvLocation.text = ubicacion
        tvDescription.text = descripcion
        val fotoNombre = fotoUrl?.substringBeforeLast('.')?.lowercase()
        val fotoResId = fotoNombre?.let {
            resources.getIdentifier(it, "drawable", packageName)
        } ?: 0

        Glide.with(this)
            .load(if (fotoResId != 0) fotoResId else R.drawable.maluma) // fallback
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(R.drawable.maluma)
            .into(ivEventImage)

        // 5. Listeners
        btnBack.setOnClickListener { finish() }

        btnComprar.setOnClickListener {
            val intentCompra = Intent(this, PaginaCompraActivity::class.java).apply {
                putExtra("EVENTO_ID", eventoId)
                putExtra("TITULO", nombre)
                putExtra("FECHA", fechaLimpia)
                putExtra("LUGAR", ubicacion)
                putExtra("IMAGEN_URL", fotoUrl)
            }
            startActivity(intentCompra)
        }

    }
}