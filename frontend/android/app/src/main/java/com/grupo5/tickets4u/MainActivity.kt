package com.grupo5.tickets4u

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
// IMPORT CORREGIDO: Basado en tu estructura de carpetas eventos.ui.cart
import com.grupo5.tickets4u.eventos.ui.cart.CartActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var destacadosRecycler: RecyclerView
    private lateinit var actualesRecycler: RecyclerView
    private lateinit var internacionalesRecycler: RecyclerView

    // 🚀 Nuevo: launcher para recibir el QR escaneado
    private val qrScannerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val qrCode = result.data?.getStringExtra("QR_CODE")
            qrCode?.let {
                validarQrEnBackend(it)
            }
        }
    }
    private var isEditModeActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbarAndDrawer()
        setupRecyclerViews()
        setupQrScanner()
        setupCrearEventoButton()
        fetchEventos()

    // ✅ Abre el escáner QR (tu QrScannerActivity)
    private fun setupQrScanner() {
        findViewById<Button>(R.id.btnScanQr).setOnClickListener {
            val intent = Intent(this, QrScannerActivity::class.java)
            qrScannerLauncher.launch(intent)
        }
    }

    // ✅ Mismo método de antes: valida con backend
    private fun validarQrEnBackend(qrCode: String) {
        lifecycleScope.launch {
            try {
                Log.d("QR_VALIDATION", "Validando QR: $qrCode")
                val response = RetrofitClient.instance.validarQr(qrCode)

                when {
                    response.status == "VALIDO" -> {
                        Toast.makeText(this@MainActivity, "✅ Ticket válido", Toast.LENGTH_LONG).show()
                    }
                    response.status == "INVALIDO" -> {
                        Toast.makeText(this@MainActivity, "❌ Ticket inválido", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "⚠️ ${response.status}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("QR_ERROR", "Error API: ${e.message}")
                Toast.makeText(this@MainActivity, "Error conexión backend", Toast.LENGTH_SHORT).show()
            }
        }
    }
        fetchEventos()

        // BOTÓN GESTIÓN (ÍCONO LÁPIZ)
        findViewById<ImageButton>(R.id.btn_gestion_eventos).setOnClickListener { view ->
            isEditModeActive = !isEditModeActive
            // Cambia el color del icono para indicar si el modo edición está activo
            val color = if (isEditModeActive) Color.RED else Color.WHITE
            (view as ImageButton).setColorFilter(color)

            updateAdaptersEditMode()
            Toast.makeText(this, if(isEditModeActive) "Modo Edición Activado" else "Modo Vista Activado", Toast.LENGTH_SHORT).show()
        }

        // BOTÓN ABRIR FORMULARIO (NUEVO EVENTO)
        findViewById<Button>(R.id.btnAbrirFormulario).setOnClickListener {
            openEventDialog(null)
        }
    }

    private fun setupToolbarAndDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = getColor(android.R.color.white)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> fetchEventos()
                R.id.nav_tickets -> startActivity(Intent(this, TicketWallet::class.java))
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // ACCESO AL CARRITO
        findViewById<ImageView>(R.id.toolbar_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
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
        lifecycleScope.launch {
            try {
                val lista = RetrofitClient.instance.getEventos()
                setupAdapters(lista)
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Error desconocido")
                Toast.makeText(this@MainActivity, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAdapters(lista: List<Event>) {
        val onEdit = { e: Event -> openEventDialog(e) }
        val onDelete = { e: Event -> confirmDelete(e) }

        // Filtrado por categorías según la API
        destacadosRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("DESTACADO", true) }, onEdit, onDelete)
        actualesRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("ACTUAL", true) }, onEdit, onDelete)
        internacionalesRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("INTERNACIONAL", true) }, onEdit, onDelete)

        updateAdaptersEditMode()
    }

    private fun updateAdaptersEditMode() {
        // Notifica a los adaptadores si deben mostrar u ocultar los botones de editar/borrar
        (destacadosRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (actualesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (internacionalesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
    }


    private fun setupAdapters(lista: List<Event>) {
        val onEdit = { e: Event -> openEventDialog(e) }
        val onDelete = { e: Event -> confirmDelete(e) }

        // Filtrado por categorías según la API
        destacadosRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("DESTACADO", true) }, onEdit, onDelete)
        actualesRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("ACTUAL", true) }, onEdit, onDelete)
        internacionalesRecycler.adapter = EventAdapter(lista.filter { it.categoria.equals("INTERNACIONAL", true) }, onEdit, onDelete)

        updateAdaptersEditMode()
    }

    private fun updateAdaptersEditMode() {
        // Notifica a los adaptadores si deben mostrar u ocultar los botones de editar/borrar
        (destacadosRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (actualesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
        (internacionalesRecycler.adapter as? EventAdapter)?.setEditMode(isEditModeActive)
    }

    private fun openEventDialog(event: Event?) {
        // Se llama al diálogo de creación/edición pasándole la función de refresco
        val dialog = CrearEventoDialogFragment(
            eventoParaEditar = event,
            onEventoGuardado = { fetchEventos() }
        )
        dialog.show(supportFragmentManager, "EventDialog")
    }

    private fun confirmDelete(event: Event) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar '${event.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val resp = RetrofitClient.instance.eliminarEvento(event.id ?: 0L)
                        if (resp.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Evento eliminado", Toast.LENGTH_SHORT).show()
                            fetchEventos() // Refresca la lista tras borrar
                        }
                    } catch (e: Exception) {
                        Log.e("DELETE_ERROR", e.message ?: "Error")
                        Toast.makeText(this@MainActivity, "Error al eliminar evento", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
