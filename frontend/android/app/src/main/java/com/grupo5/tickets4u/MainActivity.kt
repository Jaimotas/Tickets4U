    package com.grupo5.tickets4u

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
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

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            setupToolbarAndDrawer()
            setupRecyclerViews()
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

        override fun onPostCreate(savedInstanceState: Bundle?) {
            super.onPostCreate(savedInstanceState)
            toggle.syncState()
        }
    }
