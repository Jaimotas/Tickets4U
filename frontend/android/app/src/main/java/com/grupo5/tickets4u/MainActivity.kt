package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    // Recyclers
    private lateinit var destacadosRecycler: RecyclerView
    private lateinit var actualesRecycler: RecyclerView
    private lateinit var internacionalesRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbarAndDrawer()
        setupRecyclerViews()

        // Llamada al backend
        fetchEventos()
    }

    private fun setupToolbarAndDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = getColor(android.R.color.white)

        // LISTENER ACTUALIZADO (SIN nav_perfil)
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

        // Flechas de scroll manual
        findViewById<ImageView>(R.id.arrow_eventos_actuales).setOnClickListener {
            actualesRecycler.smoothScrollBy(500, 0)
        }
        findViewById<ImageView>(R.id.arrow_eventos_internacionales).setOnClickListener {
            internacionalesRecycler.smoothScrollBy(500, 0)
        }
    }

    private fun setupRecyclerViews() {
        destacadosRecycler = findViewById(R.id.eventos_recyclerview)
        destacadosRecycler.layoutManager = LinearLayoutManager(this)

        actualesRecycler = findViewById(R.id.eventos_actuales_recyclerview)
        actualesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        internacionalesRecycler = findViewById(R.id.mas_eventos_recyclerview)
        internacionalesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchEventos() {
        // Ejecutamos en segundo plano
        lifecycleScope.launch {
            try {
                val listaEventos = RetrofitClient.instance.getEventos()

                if (listaEventos.isNotEmpty()) {
                    // 1. Destacados (ej. los primeros 3)
                    destacadosRecycler.adapter = EventAdapter(listaEventos.take(3))

                    // 2. Actuales (ej. los que son en Madrid)
                    val locales = listaEventos.filter { it.location.contains("Madrid", ignoreCase = true) }
                    actualesRecycler.adapter = EventAdapter(locales)

                    // 3. Internacionales (ej. los que NO son en Madrid)
                    val internacionales = listaEventos.filter { !it.location.contains("Madrid", ignoreCase = true) }
                    internacionalesRecycler.adapter = EventAdapter(internacionales)
                }

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al cargar eventos: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }
}
