package com.grupo5.tickets4u

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PaginaCompraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_compra)

        // 1. Referencias de la UI
        val tvTitulo = findViewById<TextView>(R.id.tvTituloCompra)
        val tvFecha = findViewById<TextView>(R.id.tvFechaCompra)
        val tvLugar = findViewById<TextView>(R.id.tvLugarCompra)
        val ivImagen = findViewById<ImageView>(R.id.ivImagenCompra)
        val spinner = findViewById<Spinner>(R.id.spinnerEntradas)
        val btnFinalizar = findViewById<Button>(R.id.btnFinalizarCompra)

        // 2. Recibir datos del Intent
        val titulo = intent.getStringExtra("TITULO")
        val fecha = intent.getStringExtra("FECHA")
        val lugar = intent.getStringExtra("LUGAR")
        val imagenRes = intent.getIntExtra("IMAGEN_RES", 0)

        // 3. Asignar datos dinámicamente
        tvTitulo.text = titulo
        tvFecha.text = fecha
        tvLugar.text = lugar
        ivImagen.setImageResource(imagenRes)

        // 4. Configurar el Spinner (1 a 8)
        val opciones = (1..8).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 5. Botón Comprar con Modal
        btnFinalizar.setOnClickListener {
            val cantidad = spinner.selectedItem.toString()

            AlertDialog.Builder(this)
                .setTitle("¡Compra exitosa!")
                .setMessage("Has comprado $cantidad entradas para $titulo.")
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                    finish() // Opcional: cierra esta pantalla tras comprar
                }
                .show()
        }
    }
}