package com.grupo5.tickets4u.eventos.ui.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.grupo5.tickets4u.R
import com.grupo5.tickets4u.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

        // 2. Configuración inicial - OBTENER DATOS DEL INTENT
        val total = intent.getDoubleExtra("TOTAL_CARRITO", 0.0)
        val eventoId = intent.getLongExtra("EVENTO_ID", 0L)

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

        // 4. Lógica de Pago - MODIFICADO PARA LLAMAR AL BACKEND
        btnConfirmarPago.setOnClickListener {
            btnConfirmarPago.isEnabled = false
            confirmarPedidoEnBackend(total, eventoId, btnConfirmarPago)
        }
    }

    // 👇 FUNCIÓN ACTUALIZADA CON ResponseBody
    private fun confirmarPedidoEnBackend(total: Double, eventoId: Long, boton: Button) {
        // Verificar que el usuario esté autenticado
        val prefs = getSharedPreferences("TICKETS4U_PREFS", Context.MODE_PRIVATE)
        val token = prefs.getString("TOKEN", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Debes iniciar sesión primero", Toast.LENGTH_LONG).show()
            boton.isEnabled = true
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Llamada al backend
                val responseBody = ApiService.RetrofitClient.instance.confirmarPedido(
                    total = total,
                    idEvento = eventoId
                )

                // Leer el ResponseBody como string
                val responseText = responseBody.string()
                Log.d("PEDIDO_SUCCESS", "Respuesta del servidor: $responseText")

                // Si todo OK, ir a la pantalla de loading/success
                runOnUiThread {
                    val intentLoading = Intent(this@PaymentActivity, PaymentLoadingActivity::class.java)
                    intentLoading.putExtra("TOTAL_PAGADO", total)
                    intentLoading.putExtra("PEDIDO_CONFIRMADO", true)
                    startActivity(intentLoading)
                    finish()
                }

            } catch (e: HttpException) {
                Log.e("PEDIDO_ERROR", "HTTP Error ${e.code()}: ${e.message()}")

                runOnUiThread {
                    boton.isEnabled = true

                    when (e.code()) {
                        401 -> {
                            Toast.makeText(
                                this@PaymentActivity,
                                "Sesión expirada. Vuelve a iniciar sesión",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        400 -> {
                            Toast.makeText(
                                this@PaymentActivity,
                                "Error en los datos del pedido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this@PaymentActivity,
                                "Error al confirmar pedido. Intenta de nuevo",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("PEDIDO_ERROR", "Error: ${e.message}", e)

                runOnUiThread {
                    boton.isEnabled = true
                    Toast.makeText(
                        this@PaymentActivity,
                        "Error de conexión. Verifica tu internet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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