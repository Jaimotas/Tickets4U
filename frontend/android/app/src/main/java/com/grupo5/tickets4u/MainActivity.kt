package com.grupo5.tickets4u

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.grupo5.tickets4u.eventos.ui.cart.CartActivity
import kotlinx.coroutines.launch
import android.content.Context
import android.view.View


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var destacadosRecycler: RecyclerView
    private lateinit var actualesRecycler: RecyclerView
    private lateinit var internacionalesRecycler: RecyclerView
    private var isEditModeActive = false
    private var userRole: String = "CLIENTE"

    private val qrScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val qrCode = result.data?.getStringExtra("QR_CODE")
            qrCode?.let { validarQrEnBackend(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("TICKETS4U_PREFS", Context.MODE_PRIVATE)
        userRole = prefs.getString("USER_ROLE", "CLIENTE") ?: "CLIENTE"

        setupToolbarAndDrawer()
        setupRecyclerViews()
        setupCrearEventoButton()
        fetchEventos()
    }

    private fun setupCrearEventoButton() {
        val btnAbrirFormulario = findViewById<Button>(R.id.btnAbrirFormulario)
        val btnGestionEventos = findViewById<ImageButton>(R.id.btn_gestion_eventos)
        val btnScanQr = findViewById<Button>(R.id.btnScanQr)

        val esAdmin = userRole.equals("admin", ignoreCase = true)
        val visibilidad = if (esAdmin) View.VISIBLE else View.GONE

        btnAbrirFormulario.visibility = visibilidad
        btnGestionEventos.visibility = visibilidad
        btnScanQr.visibility = visibilidad

        // Listeners
        btnAbrirFormulario.setOnClickListener { openEventDialog(null) }
        btnScanQr.setOnClickListener {
            qrScannerLauncher.launch(Intent(this, QrScannerActivity::class.java))
        }
        btnGestionEventos.setOnClickListener { view ->
            isEditModeActive = !isEditModeActive
            (view as ImageButton).setColorFilter(if (isEditModeActive) Color.RED else Color.WHITE)
            updateAdaptersEditMode()
        }

        // Listeners comunes
        findViewById<ImageView>(R.id.toolbar_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        findViewById<ImageView>(R.id.toolbar_profile).setOnClickListener {
            Toast.makeText(this, "Perfil: $userRole", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbarAndDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // QUITAR TÍTULO DUPLICADO
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.drawerArrowDrawable.color = Color.WHITE
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
    }

    private fun setupAdapters(lista: List<Event>) {
        val onEdit = { e: Event -> openEventDialog(e) }
        val onDelete = { e: Event -> confirmDelete(e) }

        // Si la base de datos está vacía, estas listas estarán vacías
        destacadosRecycler.adapter = EventAdapter(lista.filter { it.categoria?.uppercase() == "DESTACADO" }, onEdit, onDelete)
        actualesRecycler.adapter = EventAdapter(lista.filter { it.categoria?.uppercase() == "ACTUAL" }, onEdit, onDelete)
        internacionalesRecycler.adapter = EventAdapter(lista.filter { it.categoria?.uppercase() == "INTERNACIONAL" }, onEdit, onDelete)

        updateAdaptersEditMode()
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
                val lista = ApiService.RetrofitClient.instance.getEventos()
                setupAdapters(lista)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener eventos: ${e.message}")
                if (e.message?.contains("401") == true) {
                    Toast.makeText(this@MainActivity, "Error 401: Revisa el BackendApplication corregido.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateAdaptersEditMode() {
        (destacadosRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (actualesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (internacionalesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
    }

    private fun openEventDialog(event: Event?) {
        if (userRole != "admin") {
            Toast.makeText(this, "No tienes permisos de administrador", Toast.LENGTH_SHORT).show()
            return
        }
        CrearEventoDialogFragment(event) { fetchEventos() }.show(supportFragmentManager, "EventDialog")
    }

    private fun confirmDelete(event: Event) {
        if (userRole != "admin") return // Seguridad extra

        AlertDialog.Builder(this)
            .setTitle("Eliminar Evento")
            .setMessage("¿Estás seguro de eliminar ${event.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    try {
                        ApiService.RetrofitClient.instance.eliminarEvento(event.id ?: 0L)
                        fetchEventos()
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.setNegativeButton("Cancelar", null).show()
    }

    private fun validarQrEnBackend(qrCode: String) {
        if (!userRole.equals("admin", ignoreCase = true)) {
            Toast.makeText(this, "No tienes permiso para validar", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = ApiService.RetrofitClient.instance.validarQr(qrCode)
                if (response.status == "VALIDO") {
                    mostrarResultadoValidacion("✅ Ticket Válido", "El ticket ha sido verificado correctamente.")
                } else {
                    mostrarResultadoValidacion("❌ Ticket Inválido", "Motivo: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("QR_ERROR", "Fallo al validar: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarResultadoValidacion(titulo: String, mensaje: String) {
        AlertDialog.Builder(this).setTitle(titulo).setMessage(mensaje).setPositiveButton("Aceptar", null).show()
    }
}