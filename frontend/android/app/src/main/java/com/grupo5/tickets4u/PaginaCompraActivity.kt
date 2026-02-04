package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.grupo5.tickets4u.eventos.ui.cart.CartManager
import com.grupo5.tickets4u.model.TicketItem

class PaginaCompraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_pagina_compra)

        // 1. Referencias de la UI
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvTitulo = findViewById<TextView>(R.id.tvTituloCompra)
        val tvFecha = findViewById<TextView>(R.id.tvFechaCompra)
        val tvLugar = findViewById<TextView>(R.id.tvLugarCompra)
        val ivImagen = findViewById<ImageView>(R.id.ivImagenCompra)
        val autoCompleteEntradas = findViewById<AutoCompleteTextView>(R.id.autoCompleteEntradas)
        val btnFinalizar = findViewById<MaterialButton>(R.id.btnFinalizarCompra)

        // 2. Recibir datos del Intent
        val eventoId = intent.getLongExtra("EVENTO_ID", -1)
        val titulo = intent.getStringExtra("TITULO") ?: "Evento"
        val fecha = intent.getStringExtra("FECHA") ?: "Fecha pendiente"
        val lugar = intent.getStringExtra("LUGAR") ?: "Lugar pendiente"
        val fotoNombre = intent.getStringExtra("IMAGEN_URL")

        // 3. Convertir el nombre de la DB a resource ID
        val fotoResId = fotoNombre?.substringBeforeLast('.')?.lowercase()?.let { nombre ->
            resources.getIdentifier(nombre, "drawable", packageName)
        } ?: 0

        // 4. Asignar datos a la UI
        tvTitulo.text = titulo
        tvFecha.text = fecha
        tvLugar.text = lugar

        Glide.with(this)
            .load(if (fotoResId != 0) fotoResId else R.drawable.maluma)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(R.drawable.maluma)
            .into(ivImagen)

        // 5. Configurar el Dropdown de cantidad (Máximo 8)
        val opciones = arrayOf(
            "1 entrada", "2 entradas", "3 entradas", "4 entradas",
            "5 entradas", "6 entradas", "7 entradas", "8 entradas"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        autoCompleteEntradas.setAdapter(adapter)

        // 6. Botón Volver
        btnBack.setOnClickListener { finish() }

        // 7. Lógica de añadir al Carrito con COMPROBACIÓN DE LÍMITE
        btnFinalizar.setOnClickListener {
            val seleccionStr = autoCompleteEntradas.text.toString()
            val cantidadSeleccionada = seleccionStr.filter { it.isDigit() }.toIntOrNull() ?: 1
            val precioSimulado = 25.0

            // --- COMPROBACIÓN EN EL CARRITO ---
            val itemsEnCarrito = CartManager.getItems()
            val itemExistente = itemsEnCarrito.find { it.id == eventoId.toString() }
            val cantidadActual = itemExistente?.cantidad ?: 0
            val totalResultante = cantidadActual + cantidadSeleccionada

            if (totalResultante > 8) {
                val disponibles = 8 - cantidadActual
                val mensaje = if (disponibles > 0) {
                    "No puedes añadir $cantidadSeleccionada entradas. Ya tienes $cantidadActual en el carrito. Solo puedes añadir $disponibles más."
                } else {
                    "Ya tienes el máximo de 8 entradas para este evento en tu carrito."
                }
                AlertDialog.Builder(this)
                    .setTitle("Límite alcanzado")
                    .setMessage(mensaje)
                    .setPositiveButton("Entendido", null)
                    .show()
                return@setOnClickListener
            }
            // --- FIN COMPROBACIÓN ---

            AlertDialog.Builder(this)
                .setTitle("¿Añadir al carrito?")
                .setMessage("Vas a añadir $cantidadSeleccionada entradas para $titulo.\nTotal: ${precioSimulado * cantidadSeleccionada}€")
                .setPositiveButton("Añadir") { _, _ ->

                    val nuevoTicket = TicketItem(
                        id = eventoId.toString(),
                        nombreEvento = titulo,
                        precio = precioSimulado,
                        cantidad = cantidadSeleccionada,
                        imagenUrl = fotoNombre,
                        eventoId = eventoId
                    )

                    CartManager.addItem(nuevoTicket)
                    Toast.makeText(this, "Añadido al carrito", Toast.LENGTH_SHORT).show()

                    val intentMain = Intent(this, MainActivity::class.java)
                    intentMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intentMain)
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
