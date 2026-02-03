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
import com.grupo5.tickets4u.eventos.ui.cart.CartActivity // Asegúrate de que esta ruta es correcta
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var destacadosRecycler: RecyclerView
    private lateinit var actualesRecycler: RecyclerView
    private lateinit var internacionalesRecycler: RecyclerView
    private var isEditModeActive = false

    private val qrScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val qrCode = result.data?.getStringExtra("QR_CODE")
            qrCode?.let { validarQrEnBackend(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbarAndDrawer()
        setupRecyclerViews()
        fetchEventos()

        // --- LISTENERS DEL TOOLBAR (CARRITO Y PERFIL) ---
        findViewById<ImageView>(R.id.toolbar_cart).setOnClickListener {
            Log.d("NAV", "Abriendo Carrito")
            // Intent para abrir tu actividad de carrito
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<ImageView>(R.id.toolbar_profile).setOnClickListener {
            Toast.makeText(this, "Perfil de usuario (Próximamente)", Toast.LENGTH_SHORT).show()
        }

        // --- OTROS BOTONES ---
        findViewById<Button>(R.id.btnScanQr).setOnClickListener {
            qrScannerLauncher.launch(Intent(this, QrScannerActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btn_gestion_eventos).setOnClickListener { view ->
            isEditModeActive = !isEditModeActive
            (view as ImageButton).setColorFilter(if (isEditModeActive) Color.RED else Color.WHITE)
            updateAdaptersEditMode()
        }

        findViewById<Button>(R.id.btnAbrirFormulario).setOnClickListener { openEventDialog(null) }
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
            if (it.itemId == R.id.nav_tickets) startActivity(Intent(this, TicketWallet::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun fetchEventos() {
        lifecycleScope.launch {
            try {
                val lista = RetrofitClient.instance.getEventos()
                setupAdapters(lista)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al obtener eventos: ${e.message}")
                if (e.message?.contains("401") == true) {
                    Toast.makeText(this@MainActivity, "Error 401: Revisa el BackendApplication corregido.", Toast.LENGTH_LONG).show()
                }
            }
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
        actualesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        internacionalesRecycler = findViewById(R.id.mas_eventos_recyclerview)
        internacionalesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun updateAdaptersEditMode() {
        (destacadosRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (actualesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (internacionalesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
    }

    private fun openEventDialog(event: Event?) {
        CrearEventoDialogFragment(event) { fetchEventos() }.show(supportFragmentManager, "EventDialog")
    }

    private fun confirmDelete(event: Event) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Evento")
            .setMessage("¿Estás seguro de eliminar ${event.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    try {
                        RetrofitClient.instance.eliminarEvento(event.id ?: 0L)
                        fetchEventos()
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.setNegativeButton("Cancelar", null).show()
    }

    private fun validarQrEnBackend(qrCode: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.validarQr(qrCode)
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