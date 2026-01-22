package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.grupo5.tickets4u.ui.cart.CartActivity


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var internacionalesRecycler: RecyclerView
    private lateinit var actualesRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- TOOLBAR Y TÍTULO ---
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // --- DRAWER Y MENÚ HAMBURGUESA ---
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = getColor(android.R.color.white)

        // LISTENER DRAWER
        navView.setNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId) {
                R.id.nav_home -> { }
                R.id.nav_tickets -> { }
                R.id.nav_settings -> { }
                R.id.nav_help -> { }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // --- CONFIGURACIÓN DE RECYCLERVIEWS ---
        val destacadosRecycler = findViewById<RecyclerView>(R.id.eventos_recyclerview)
        destacadosRecycler.layoutManager = LinearLayoutManager(this)
        destacadosRecycler.adapter = EventAdapter(createFeaturedEvents())

        actualesRecycler = findViewById(R.id.eventos_actuales_recyclerview)
        actualesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        actualesRecycler.adapter = EventAdapter(createCurrentEvents())

        internacionalesRecycler = findViewById(R.id.mas_eventos_recyclerview)
        internacionalesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        internacionalesRecycler.adapter = EventAdapter(createInternationalEvents())

        // --- CLICS DE FLECHAS E ICONOS ---
        findViewById<ImageView>(R.id.arrow_eventos_actuales).setOnClickListener {
            actualesRecycler.smoothScrollBy(400, 0)
        }

        findViewById<ImageView>(R.id.arrow_eventos_internacionales).setOnClickListener {
            internacionalesRecycler.smoothScrollBy(400, 0)
        }

        // CARRITO DESDE TOOLBAR ✅
        findViewById<ImageView>(R.id.toolbar_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<ImageView>(R.id.toolbar_profile).setOnClickListener {
            // TODO: Ir a pantalla perfil o abrir menú
        }

        // ✅ GESTIÓN EVENTOS (al lado de "Eventos Destacados")
        findViewById<ImageButton>(R.id.btn_gestion_eventos).setOnClickListener {
            Toast.makeText(this, "🔧 Gestión eventos (CRUD próximamente)", Toast.LENGTH_SHORT).show()
            // TODO: Cuando tengas CRUD listo:
            // startActivity(Intent(this, GestionEventosActivity::class.java))
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    // --- MÉTODOS PARA DATOS DE EJEMPLO ---
    private fun createFeaturedEvents(): List<Event> =
        listOf(
            Event(id = 1, name = "Concierto Estopa", location = "WiZink Center · Madrid", date = "10 Mar 2026 · 21:00", imageResId = R.drawable.estopa, isTrending = true),
            Event(id = 2, name = "Mad Cool Festival", location = "IFEMA · Madrid", date = "15 Jul 2026 · 18:00", imageResId = R.drawable.madcool, isTrending = true),
            Event(id = 3, name = "La Brama del Cèrvol", location = "Teatro Principal · Barcelona", date = "20 Feb 2026 · 20:00", imageResId = R.drawable.la_brama_del_cervol, isTrending = true)
        )

    private fun createCurrentEvents(): List<Event> =
        listOf(
            Event(id = 101, name = "Anuel AA – Las Leyendas Nunca Mueren Tour", location = "Palacio Vistalegre · Madrid", date = "05 Abr 2026 · 21:00", imageResId = R.drawable.anuel, isTrending = true),
            Event(id = 102, name = "Eladio Carrión – DON KBRON ", location = "WiZink Center · Madrid", date = "28 Enero 2026 · 21:30", imageResId = R.drawable.eladio),
            Event(id = 103, name = "DEIV – Tour Europa", location = "La Riviera · Madrid", date = "12 May 2026 · 20:30", imageResId = R.drawable.deiv),
            Event(id = 104, name = "Maluma – La Vida es Una Tour", location = "Palau Sant Jordi · Barcelona", date = "25 May 2026 · 21:00", imageResId = R.drawable.maluma)
        )

    private fun createInternationalEvents(): List<Event> =
        listOf(
            Event(id = 201, name = "Bruno Mars – World Tour", location = "Wembley Stadium · Londres", date = "10 Ago 2026 · 20:00", imageResId = R.drawable.bruno, isTrending = true),
            Event(id = 202, name = "The Weekend – After Hours Til Dawn", location = "Accor Arena · París", date = "18 Ago 2026 · 21:00", imageResId = R.drawable.the_weekend, isTrending = true),
            Event(id = 203, name = "Coldplay – Music of the Spheres", location = "Allianz Parque · São Paulo", date = "05 Sep 2026 · 20:30", imageResId = R.drawable.coldplay),
            Event(id = 204, name = "Dua Lipa – Future Nostalgia Tour", location = "Madison Square Garden · Nueva York", date = "20 Sep 2026 · 21:00", imageResId = R.drawable.dua_lipa)
        )
}
