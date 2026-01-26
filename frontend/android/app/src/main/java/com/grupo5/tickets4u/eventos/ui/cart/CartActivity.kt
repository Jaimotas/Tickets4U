package com.grupo5.tickets4u.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grupo5.tickets4u.R
import com.grupo5.tickets4u.eventos.ui.payment.PaymentActivity
import com.grupo5.tickets4u.model.TicketItem // Asegúrate de que esta ruta sea correcta

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
            val totalValue = viewModel.total.value ?: 0.0
            if (totalValue > 0) {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("TOTAL_CARRITO", totalValue)
                startActivity(intent)
            } else {
                // Opcional: Avisar que el carrito está vacío
            }
        }

        // 6. Datos de prueba (Cárgalos solo si el carrito está vacío para evitar duplicados)
        if (viewModel.items.value.isNullOrEmpty()) {
            viewModel.addItem(TicketItem("1", "Concierto Rock", 25.0, 1))
            viewModel.addItem(TicketItem("2", "Festival EDM", 35.0, 2))
        }
    }
}