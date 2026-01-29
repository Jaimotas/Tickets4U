package com.grupo5.tickets4u

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import android.widget.Button
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
import com.grupo5.tickets4u.ui.cart.CartActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var destacadosRecycler: RecyclerView
    private lateinit var actualesRecycler: RecyclerView
    private lateinit var internacionalesRecycler: RecyclerView

    private var isEditModeActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbarAndDrawer()
        setupRecyclerViews()
        fetchEventos()

        // BOTÓN LÁPIZ PRINCIPAL
        findViewById<ImageButton>(R.id.btn_gestion_eventos).setOnClickListener { view ->
            isEditModeActive = !isEditModeActive
            val color = if (isEditModeActive) Color.RED else Color.WHITE
            (view as ImageButton).setColorFilter(color)

            updateAdaptersEditMode()
            Toast.makeText(this, if(isEditModeActive) "Modo Edición Activado" else "Modo Vista Activado", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnAbrirFormulario).setOnClickListener {
            openEventDialog(null)
        }
        setupCrearEventoButton()
        fetchEventos()
    }

    private fun setupCrearEventoButton() {
        findViewById<Button>(R.id.btnAbrirFormulario).setOnClickListener {
            val dialog = CrearEventoDialogFragment().apply {
                onEventoCreado = { fetchEventos() }
            }
            dialog.show(supportFragmentManager, "CrearEventoDialog")
        }
    }

    private fun setupToolbarAndDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = getColor(android.R.color.white)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> fetchEventos()
                R.id.nav_tickets -> startActivity(Intent(this, TicketWallet::class.java))
                R.id.nav_settings -> Toast.makeText(this, "Ajustes", Toast.LENGTH_SHORT).show()
                R.id.nav_help -> Toast.makeText(this, "Acerca de", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        findViewById<ImageView>(R.id.toolbar_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun setupRecyclerViews() {
        destacadosRecycler = findViewById(R.id.eventos_recyclerview)
        destacadosRecycler.layoutManager = LinearLayoutManager(this)

        actualesRecycler = findViewById(R.id.eventos_actuales_recyclerview)
        actualesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        internacionalesRecycler = findViewById(R.id.mas_eventos_recyclerview)
        internacionalesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchEventos() {
        lifecycleScope.launch {
            try {
                val listaEventos = RetrofitClient.instance.getEventos()
                setupAdapters(listaEventos)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                val eventos = RetrofitClient.instance.getEventos()

                destacadosRecycler.adapter =
                    EventAdapter(eventos.filter { it.categoria.equals("DESTACADO", true) })

                actualesRecycler.adapter =
                    EventAdapter(eventos.filter { it.categoria.equals("ACTUAL", true) })

                internacionalesRecycler.adapter =
                    EventAdapter(eventos.filter { it.categoria.equals("INTERNACIONAL", true) })

            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Error")
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAdapters(lista: List<Event>) {
        val onEdit = { e: Event -> openEventDialog(e) }
        val onDelete = { e: Event -> confirmDelete(e) }

        destacadosRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("DESTACADO", true) }, onEdit, onDelete)
        actualesRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("ACTUAL", true) }, onEdit, onDelete)
        internacionalesRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("INTERNACIONAL", true) }, onEdit, onDelete)

        updateAdaptersEditMode()
    }

    private fun updateAdaptersEditMode() {
        (destacadosRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (actualesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (internacionalesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
    }

    private fun openEventDialog(event: Event?) {
        val dialog = CrearEventoDialogFragment(
            eventoParaEditar = event,
            onEventoGuardado = { fetchEventos() }
        )
        dialog.show(supportFragmentManager, "EventDialog")
    }

    private fun confirmDelete(event: Event) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar evento")
            .setMessage("¿Estás seguro de que deseas eliminar '${event.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.instance.eliminarEvento(event.id ?: 0L)
                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Eliminado con éxito", Toast.LENGTH_SHORT).show()
                            fetchEventos()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }
}
