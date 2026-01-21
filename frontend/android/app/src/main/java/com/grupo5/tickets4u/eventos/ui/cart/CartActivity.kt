package com.grupo5.tickets4u.ui.cart

import android.view.WindowManager
import android.content.Intent
import android.os.Bundle
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

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ MÉTODO DEFINITIVO
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_cart)

        // ✅ FLECHA ATRÁS
        val btnAtras = findViewById<ImageButton>(R.id.btnAtras)
        btnAtras.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val rvCart = findViewById<RecyclerView>(R.id.rvCart)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        adapter = CartAdapter(viewModel)
        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = adapter

        viewModel.items.observe(this, Observer { items ->
            adapter.updateItems(items)
        })

        viewModel.total.observe(this, Observer { total ->
            txtTotal.text = "Total: %.2f€".format(total)
        })

        btnPagar.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("TOTAL_CARRITO", viewModel.total.value ?: 0.0)
            startActivity(intent)
        }

        // Entradas de ejemplo
        viewModel.addItem(com.grupo5.tickets4u.model.TicketItem("1", "Concierto Rock", 25.0, 1))
        viewModel.addItem(com.grupo5.tickets4u.model.TicketItem("2", "Festival EDM", 35.0, 2))
    }
}