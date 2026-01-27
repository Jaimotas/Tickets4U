package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class PaginaCompraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_pagina_compra)

        // 1. Referencias
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvTitulo = findViewById<TextView>(R.id.tvTituloCompra)
        val tvFecha = findViewById<TextView>(R.id.tvFechaCompra)
        val tvLugar = findViewById<TextView>(R.id.tvLugarCompra)
        val ivImagen = findViewById<ImageView>(R.id.ivImagenCompra)
        val autoCompleteEntradas = findViewById<AutoCompleteTextView>(R.id.autoCompleteEntradas)
        val btnFinalizar = findViewById<MaterialButton>(R.id.btnFinalizarCompra)

        // 2. Recibir datos del Intent
        val titulo = intent.getStringExtra("TITULO") ?: "Tu Entrada"
        val fecha = intent.getStringExtra("FECHA") ?: "Fecha pendiente"
        val lugar = intent.getStringExtra("LUGAR") ?: "Lugar pendiente"
        val fotoUrl = intent.getStringExtra("IMAGEN_URL")

        // 3. Asignar datos
        tvTitulo.text = titulo
        tvFecha.text = fecha
        tvLugar.text = lugar

        Glide.with(this)
            .load(fotoUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(R.drawable.maluma)
            .into(ivImagen)

        // 4. Configurar el Dropdown
        val opciones = arrayOf("1 entrada", "2 entradas", "3 entradas", "4 entradas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        autoCompleteEntradas.setAdapter(adapter)

        // 5. Botón Volver (solo cierra esta pantalla)
        btnBack.setOnClickListener { finish() }

        // 6. Confirmación final y vuelta al MAIN
        btnFinalizar.setOnClickListener {
            val cantidad = autoCompleteEntradas.text.toString()

            AlertDialog.Builder(this)
                .setTitle("Confirmar Reserva")
                .setMessage("Vas a comprar $cantidad para $titulo.\n\n¿Estás de acuerdo?")
                .setPositiveButton("Sí, comprar") { _, _ ->

                    Toast.makeText(this, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()

                    // --- AQUÍ LA LÓGICA PARA VOLVER AL MAIN ---
                    val intent = Intent(this, MainActivity::class.java)

                    // Estas flags limpian el "stack" de actividades:
                    // Borra "PaginaCompra" y "DatosEvento" para que el usuario
                    // no pueda volver atrás a una compra ya hecha.
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                    startActivity(intent)
                    finish() // Cerramos esta pantalla definitivamente
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}