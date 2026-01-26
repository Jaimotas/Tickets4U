package com.grupo5.tickets4u

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class PaginaCompraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ocultar la barra superior si quieres un diseño más limpio
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

        // 2. Botón Volver
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 3. Recibir datos del Intent (enviados desde EventAdapter)
        val titulo = intent.getStringExtra("EVENTO_NOMBRE") ?: "Evento desconocido"
        val fecha = intent.getStringExtra("EVENTO_FECHA") ?: "Fecha no disponible"
        val lugar = intent.getStringExtra("EVENTO_UBICACION") ?: "Lugar no especificado"
        val fotoUrl = intent.getStringExtra("EVENTO_FOTO") // URL del backend

        // 4. Asignar datos a la UI
        tvTitulo.text = titulo
        tvFecha.text = fecha
        tvLugar.text = lugar

        // Cargar imagen con Glide (soporta URLs de internet)
        Glide.with(this)
            .load(fotoUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(R.drawable.maluma) // Imagen por defecto si falla
            .into(ivImagen)

        // 5. Configurar el Selector de cantidad (Dropdown)
        val opciones = arrayOf("1 entrada", "2 entradas", "3 entradas", "4 entradas", "5 entradas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        autoCompleteEntradas.setAdapter(adapter)

        // 6. Lógica de Finalizar Compra
        btnFinalizar.setOnClickListener {
            val seleccion = autoCompleteEntradas.text.toString()

            if (seleccion.isEmpty()) {
                Toast.makeText(this, "Por favor, selecciona cuántas entradas quieres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(this)
            builder.setTitle("¡Reserva confirmada!")
            builder.setMessage("Has reservado $seleccion para:\n$titulo\n\nPróximo paso: Realizar el pago.")
            builder.setPositiveButton("Ir al pago") { dialog, _ ->
                dialog.dismiss()
                // Aquí podrías iniciar PaymentActivity si lo deseas
                finish()
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }
    }
}