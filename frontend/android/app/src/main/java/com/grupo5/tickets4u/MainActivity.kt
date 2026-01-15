package com.grupo5.tickets4u

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Eventos destacados (3, vertical)
        val destacadosRecycler = findViewById<RecyclerView>(R.id.eventos_recyclerview)
        destacadosRecycler.layoutManager = LinearLayoutManager(this)
        destacadosRecycler.adapter = EventAdapter(createFeaturedEvents())

        // 2. Eventos actuales (4 artistas urbanos, horizontal)
        val actualesRecycler = findViewById<RecyclerView>(R.id.eventos_actuales_recyclerview)
        actualesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        actualesRecycler.adapter = EventAdapter(createCurrentEvents())

        // 3. Eventos internacionales (horizontal, como los actuales)
        val internacionalesRecycler = findViewById<RecyclerView>(R.id.mas_eventos_recyclerview)
        internacionalesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        internacionalesRecycler.adapter = EventAdapter(createInternationalEvents())

        // FLECHA DE "Eventos actuales"
        val arrowEventosActuales = findViewById<ImageView>(R.id.arrow_eventos_actuales)
        arrowEventosActuales.setOnClickListener {
            Toast.makeText(this, "Próximamente más eventos", Toast.LENGTH_SHORT).show()
            // Si quieres que además haga scroll:
            // actualesRecycler.smoothScrollBy(400, 0)
        }
    }

    // 3 eventos destacados (los 3 en tendencia)
    private fun createFeaturedEvents(): List<Event> =
        listOf(
            Event(
                id = 1,
                name = "Concierto Estopa",
                location = "WiZink Center · Madrid",
                date = "10 Mar 2026 · 21:00",
                imageResId = R.mipmap.ic_launcher_round,
                isTrending = true
            ),
            Event(
                id = 2,
                name = "Mad Cool Festival",
                location = "IFEMA · Madrid",
                date = "15 Jul 2026 · 18:00",
                imageResId = R.mipmap.ic_launcher_round,
                isTrending = true
            ),
            Event(
                id = 3,
                name = "Obra de teatro clásico",
                location = "Teatro Principal · Barcelona",
                date = "20 Feb 2026 · 20:00",
                imageResId = R.mipmap.ic_launcher_round,
                isTrending = true
            )
        )

    // 4 eventos actuales (solo ANUEL en tendencia)
    private fun createCurrentEvents(): List<Event> =
        listOf(
            Event(
                id = 101,
                name = "Anuel AA – Las Leyendas Nunca Mueren Tour",
                location = "Palacio Vistalegre · Madrid",
                date = "05 Abr 2026 · 21:00",
                imageResId = R.mipmap.ic_launcher_round,
                isTrending = true
            ),
            Event(
                id = 102,
                name = "Eladio Carrión – Sauce Boyz Live",
                location = "WiZink Center · Madrid",
                date = "20 Abr 2026 · 21:30",
                imageResId = R.mipmap.ic_launcher_round
            ),
            Event(
                id = 103,
                name = "DEIV – Tour Europa",
                location = "La Riviera · Madrid",
                date = "12 May 2026 · 20:30",
                imageResId = R.mipmap.ic_launcher_round
            ),
            Event(
                id = 104,
                name = "Myke Towers – La Vida es Una Tour",
                location = "Palau Sant Jordi · Barcelona",
                date = "25 May 2026 · 21:00",
                imageResId = R.mipmap.ic_launcher_round
            )
        )

    // 4 eventos internacionales (Bruno y The Weeknd en tendencia)
    private fun createInternationalEvents(): List<Event> =
        listOf(
            Event(
                id = 201,
                name = "Bruno Mars – World Tour",
                location = "Wembley Stadium · Londres",
                date = "10 Ago 2026 · 20:00",
                imageResId = R.mipmap.ic_launcher_round,
                isTrending = true
            ),
            Event(
                id = 202,
                name = "The Weeknd – After Hours Til Dawn",
                location = "Accor Arena · París",
                date = "18 Ago 2026 · 21:00",
                imageResId = R.mipmap.ic_launcher_round,
                isTrending = true
            ),
            Event(
                id = 203,
                name = "Coldplay – Music of the Spheres",
                location = "Allianz Parque · São Paulo",
                date = "05 Sep 2026 · 20:30",
                imageResId = R.mipmap.ic_launcher_round
            ),
            Event(
                id = 204,
                name = "Dua Lipa – Future Nostalgia Tour",
                location = "Madison Square Garden · Nueva York",
                date = "20 Sep 2026 · 21:00",
                imageResId = R.mipmap.ic_launcher_round
            )
        )
}
