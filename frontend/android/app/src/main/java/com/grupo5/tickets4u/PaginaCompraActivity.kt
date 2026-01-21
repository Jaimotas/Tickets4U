package com.grupo5.tickets4u

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class PaginaCompraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_compra)

        // 1. Referencias
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvTitulo = findViewById<TextView>(R.id.tvTituloCompra)
        val tvFecha = findViewById<TextView>(R.id.tvFechaCompra)
        val tvLugar = findViewById<TextView>(R.id.tvLugarCompra)
        val ivImagen = findViewById<ImageView>(R.id.ivImagenCompra)
        val autoCompleteEntradas = findViewById<AutoCompleteTextView>(R.id.autoCompleteEntradas)
        val btnFinalizar = findViewById<MaterialButton>(R.id.btnFinalizarCompra)

        // 2. Botón Volver
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la pantalla anterior
        }

        // 3. RECIBIR DATOS del EventAdapter ✅ (claves actualizadas)
        val titulo = intent.getStringExtra("EVENTO_NOMBRE") ?: "Evento"
        val fecha = intent.getStringExtra("EVENTO_FECHA")
        val lugar = intent.getStringExtra("EVENTO_UBICACION")
        val imagenRes = intent.getIntExtra("EVENTO_IMAGEN", 0)

        // 4. Asignar datos a la UI
        tvTitulo.text = titulo
        tvFecha.text = fecha
        tvLugar.text = lugar
        if (imagenRes != 0) {
            ivImagen.setImageResource(imagenRes)
        }

        // 5. Configurar el Selector Moderno (1 a 8)
        val opciones = arrayOf("1 entrada", "2 entradas", "3 entradas", "4 entradas", "5 entradas", "6 entradas", "7 entradas", "8 entradas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        autoCompleteEntradas.setAdapter(adapter)

        // 6. Modal de éxito al pulsar comprar
        btnFinalizar.setOnClickListener {
            val seleccion = autoCompleteEntradas.text.toString()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("¡Compra realizada!")
            builder.setMessage("Has reservado $seleccion para:\n$titulo\n\nLugar: $lugar")
            builder.setPositiveButton("Genial") { dialog, _ ->
                dialog.dismiss()
                finish() // Cierra la pantalla de compra
            }
            builder.show()
        }
    }
}
