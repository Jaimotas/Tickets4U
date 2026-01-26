package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.grupo5.tickets4u.R
import java.time.LocalDate

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_payment)

        // 1. Referencias de la UI
        val txtTotalPago = findViewById<TextView>(R.id.txtTotalPago)
        val edtTitular = findViewById<EditText>(R.id.edtTitular)
        val edtNumeroTarjeta = findViewById<EditText>(R.id.edtNumeroTarjeta)
        val edtFecha = findViewById<EditText>(R.id.edtFecha)
        val edtCvc = findViewById<EditText>(R.id.edtCvc)
        val btnConfirmarPago = findViewById<Button>(R.id.btnConfirmarPago)
        val tilFecha = findViewById<TextInputLayout>(R.id.tilFecha)
        val btnAtras = findViewById<ImageButton>(R.id.btnAtras)

        // 2. Configuración inicial
        val total = intent.getDoubleExtra("TOTAL_CARRITO", 0.0)
        txtTotalPago.text = "Total a pagar: %.2f€".format(total)

        btnAtras.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Bloquear copiar/pegar para mayor seguridad
        val disableActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = false
            override fun onDestroyActionMode(mode: ActionMode?) {}
        }

        listOf(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc).forEach { editText ->
            editText.customInsertionActionModeCallback = disableActionModeCallback
            editText.customSelectionActionModeCallback = disableActionModeCallback
            editText.isLongClickable = false
        }

        // 3. Formateadores y Vigilantes de texto (TextWatchers)

        // Formatear Tarjeta: 1234 5678...
        edtNumeroTarjeta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().replace(" ", "")
                val formatted = text.chunked(4).joinToString(" ")
                if (s.toString() != formatted) {
                    edtNumeroTarjeta.removeTextChangedListener(this)
                    edtNumeroTarjeta.setText(formatted)
                    edtNumeroTarjeta.setSelection(formatted.length)
                    edtNumeroTarjeta.addTextChangedListener(this)
                }
                validarFormulario(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc, btnConfirmarPago)
            }
        })

        // Formatear y Validar Fecha: MM/AA
        edtFecha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Auto-insertar barra /
                if (before == 0 && s?.length == 2) {
                    edtFecha.append("/")
                }
            }
            override fun afterTextChanged(s: Editable?) {
                val fechaText = s.toString().trim()
                if (fechaText.length == 5) {
                    if (!isFechaValida(fechaText)) {
                        tilFecha.error = "Fecha caducada o inválida"
                    } else {
                        tilFecha.error = null
                    }
                } else {
                    tilFecha.error = null
                }
                validarFormulario(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc, btnConfirmarPago)
            }
        })

        // Otros campos simples
        val watcherSimple = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validarFormulario(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc, btnConfirmarPago)
            }
        }
        edtTitular.addTextChangedListener(watcherSimple)
        edtCvc.addTextChangedListener(watcherSimple)

        // 4. Lógica de Pago
        btnConfirmarPago.setOnClickListener {
            btnConfirmarPago.isEnabled = false
            // Enviamos a la pantalla de carga para simular proceso bancario
            val intentLoading = Intent(this, PaymentLoadingActivity::class.java)
            intentLoading.putExtra("TOTAL_PAGADO", total)
            startActivity(intentLoading)
            finish()
        }
    }

    private fun validarFormulario(titular: EditText, numero: EditText, fecha: EditText, cvc: EditText, boton: Button) {
        boton.isEnabled = isFormValid(titular, numero, fecha, cvc)
    }

    private fun isFechaValida(fechaText: String): Boolean {
        return try {
            val partes = fechaText.split("/")
            if (partes.size != 2) return false

            val mes = partes[0].toInt()
            val ano = partes[1].toInt()

            if (mes !in 1..12) return false

            val fechaCaducidad = LocalDate.of(2000 + ano, mes, 1).plusMonths(1).minusDays(1)
            val hoy = LocalDate.now()

            fechaCaducidad.isAfter(hoy)
        } catch (e: Exception) {
            false
        }
    }

    private fun isFormValid(titular: EditText, numero: EditText, fecha: EditText, cvc: EditText): Boolean {
        val titularText = titular.text.toString().trim()
        val numeroText = numero.text.toString().replace(" ", "")
        val fechaText = fecha.text.toString().trim()
        val cvcText = cvc.text.toString()

        return titularText.isNotEmpty() &&
                numeroText.length == 16 &&
                fechaText.matches(Regex("^\\d{2}/\\d{2}$")) &&
                isFechaValida(fechaText) &&
                cvcText.length == 3
    }
}