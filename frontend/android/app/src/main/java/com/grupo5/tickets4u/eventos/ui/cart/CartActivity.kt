package com.grupo5.tickets4u.eventos.ui.cart

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

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_cart)

        val btnAtras = findViewById<ImageButton>(R.id.btnAtras)
        val rvCart = findViewById<RecyclerView>(R.id.rvCart)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        // Configurar Adapter con botones + y -
        adapter = CartAdapter(viewModel)
        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = adapter

        viewModel.items.observe(this, Observer { items ->
            adapter.updateItems(items ?: emptyList())
        })

        viewModel.total.observe(this, Observer { total ->
            txtTotal.text = "Total: %.2f€".format(total ?: 0.0)
        })

        // Sincronizar Manager con ViewModel
        CartManager.getItems().forEach { viewModel.addItem(it) }

        btnAtras.setOnClickListener { finish() }

        btnPagar.setOnClickListener {
            val totalValue = viewModel.total.value ?: 0.0
            if (totalValue > 0) {
                // Pasamos al flujo de pago (PaymentActivity)
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("TOTAL_CARRITO", totalValue)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}