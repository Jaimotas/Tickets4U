package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        val tvTicketsDisponibles = findViewById<TextView>(R.id.tvTicketsDisponibles)
        val tvTicketsVendidos = findViewById<TextView>(R.id.tvTicketsVendidos)
        val tvIngresos = findViewById<TextView>(R.id.tvIngresos)

        val barIngresos = findViewById<View>(R.id.barIngresos)
        val barVendidos = findViewById<View>(R.id.barVendidos)
        val barDisponibles = findViewById<View>(R.id.barDisponibles)
        val cardStats = findViewById<View>(R.id.cardStats)

        // 2. Recibir datos
        val eventoId = intent.getLongExtra("EVENTO_ID", -1)
        val nombre = intent.getStringExtra("EVENTO_NOMBRE")
        val fechaRaw = intent.getStringExtra("EVENTO_FECHA") ?: ""
        val ubicacion = intent.getStringExtra("EVENTO_UBICACION")
        val fotoUrl = intent.getStringExtra("EVENTO_FOTO")
        val descripcion = intent.getStringExtra("EVENTO_DESCRIPCION") ?: "Sin descripción."

        val ticketsDisponibles = intent.getIntExtra("EVENTO_TICKETS_DISPONIBLES", -1)
        val ticketsVendidos = intent.getIntExtra("EVENTO_TICKETS_VENDIDOS", -1)
        val ingresos = intent.getDoubleExtra("EVENTO_INGRESOS", -1.0)

        // 3. Formatear fecha
        val fechaLimpia = try {
            if (fechaRaw.contains("T")) {
                val partes = fechaRaw.split("T")
                val soloFecha = partes[0]
                val soloHora = partes[1].substring(0, 5)
                "$soloFecha · $soloHora"
            } else fechaRaw
        } catch (e: Exception) { fechaRaw }

        // 4. Asignar datos del evento
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

        // 4b. Estadísticas (0 si no hay datos)
        val disp = if (ticketsDisponibles >= 0) ticketsDisponibles else 0
        val vend = if (ticketsVendidos >= 0) ticketsVendidos else 0
        val ing = if (ingresos >= 0.0) ingresos else 0.0

        // Números grandes
        tvTicketsDisponibles.text = disp.toString()
        tvTicketsVendidos.text = vend.toString()
        tvIngresos.text = ing.toString()

        // 4c. Ajustar barras tipo gráfica
        val maxBase = maxOf(
            disp.toDouble(),
            vend.toDouble(),
            ing
        ).takeIf { it > 0 } ?: 1.0

        fun calcHeightPx(value: Double): Int {
            val ratio = value / maxBase
            val dp = (30 + 70 * ratio).toInt()  // barras entre 30dp y 100dp
            val scale = resources.displayMetrics.density
            return (dp * scale).toInt()
        }

        barDisponibles.layoutParams.height = calcHeightPx(disp.toDouble())
        barVendidos.layoutParams.height = calcHeightPx(vend.toDouble())
        barIngresos.layoutParams.height = calcHeightPx(ing)
        barDisponibles.requestLayout()
        barVendidos.requestLayout()
        barIngresos.requestLayout()

        // 4d. Animación de entrada (slide hacia abajo + fade)
        cardStats.alpha = 0f
        cardStats.translationY = -40f
        cardStats.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(400)
            .start()

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
