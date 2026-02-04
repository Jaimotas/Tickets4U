package com.grupo5.tickets4u.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grupo5.tickets4u.R
import com.grupo5.tickets4u.eventos.ui.payment.PaymentActivity
import com.grupo5.tickets4u.model.TicketItem

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Configuración de pantalla completa
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_cart)

        // 2. Referencias de la UI
        val btnAtras = findViewById<ImageButton>(R.id.btnAtras)
        val rvCart = findViewById<RecyclerView>(R.id.rvCart)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        // 3. Configuración del RecyclerView
        adapter = CartAdapter(viewModel)
        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = adapter

        // 4. Observadores (LiveData)
        viewModel.items.observe(this, Observer { items ->
            adapter.updateItems(items)
        })

        viewModel.total.observe(this, Observer { total ->
            txtTotal.text = "Total: %.2f€".format(total)
        })

        // 5. Botones y Navegación
        btnAtras.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnPagar.setOnClickListener {
            val items = viewModel.items.value
            val totalValue = viewModel.total.value ?: 0.0

            if (items.isNullOrEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (totalValue <= 0) {
                Toast.makeText(this, "Total inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener el eventoId del primer item del carrito
            val eventoId = items.firstOrNull()?.eventoId ?: 0L

            if (eventoId == 0L) {
                Toast.makeText(this, "Error: Evento no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("TOTAL_CARRITO", totalValue)
            intent.putExtra("EVENTO_ID", eventoId)  // 👈 PASAR EL ID DEL EVENTO
            startActivity(intent)
        }

        // 6. Datos de prueba (con eventoId incluido)
        if (viewModel.items.value.isNullOrEmpty()) {
            viewModel.addItem(TicketItem("1", "Concierto Rock", 25.0, 1, eventoId = 1L))
            viewModel.addItem(TicketItem("2", "Festival EDM", 35.0, 2, eventoId = 1L))
        }
    }
}