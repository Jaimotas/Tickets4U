package com.grupo5.tickets4u

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent

class DatosDeEventoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos_de_evento)

        // Configuración de insets (mantener igual)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnComprar = findViewById<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton>(R.id.fabBuyTickets)

        btnComprar.setOnClickListener {
            // 1. Crear el Intent
            val intent = Intent(this, PaginaCompraActivity::class.java)

            // 2. Pasar los datos dinámicamente
            intent.putExtra(
                "TITULO",
                findViewById<android.widget.TextView>(R.id.tvEventTitle).text.toString()
            )
            intent.putExtra(
                "FECHA",
                findViewById<android.widget.TextView>(R.id.tvEventDate).text.toString()
            )
            intent.putExtra(
                "LUGAR",
                findViewById<android.widget.TextView>(R.id.tvEventLocation).text.toString()
            )
            // Pasamos el ID del recurso de la imagen (puedes hacerlo más dinámico luego con URLs)
            intent.putExtra("IMAGEN_RES", R.drawable.maluma)

            // 3. Iniciar la actividad
            startActivity(intent)
        }
    }
}
