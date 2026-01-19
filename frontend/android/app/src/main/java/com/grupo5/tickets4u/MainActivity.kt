package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.grupo5.tickets4u.R
import com.grupo5.tickets4u.ui.cart.CartActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCarrito = findViewById<ImageButton>(R.id.btnCarrito)
        btnCarrito.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
