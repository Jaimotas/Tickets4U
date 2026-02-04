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
import java.util.Calendar

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_payment)

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

        btnAtras.setOnClickListener { finish() }

        // 1. Bloqueo de acciones no seguras
        val safeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = false
            override fun onDestroyActionMode(mode: ActionMode?) {}
        }

        listOf(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc).forEach {
            it.customInsertionActionModeCallback = safeCallback
            it.customSelectionActionModeCallback = safeCallback
            it.isLongClickable = false
        }
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

        // 3. Formateador de Fecha
        edtFecha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before == 0 && s?.length == 2) {
                    edtFecha.append("/")
                }
            }
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length == 5) {
                    if (!isFechaValida(input)) tilFecha.error = "Tarjeta caducada"
                    else tilFecha.error = null
                } else {
                    tilFecha.error = null
                }
                validarFormulario(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc, btnConfirmarPago)
            }
        })

        // 4. Otros campos
        val genericWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = validarFormulario(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc, btnConfirmarPago)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        edtTitular.addTextChangedListener(genericWatcher)
        edtCvc.addTextChangedListener(genericWatcher)

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
        val isNameValid = titular.text.trim().length > 3
        val isCardValid = numero.text.toString().replace(" ", "").length == 16
        val isDateValid = fecha.text.length == 5 && isFechaValida(fecha.text.toString())
        val isCvcValid = cvc.text.length == 3

        boton.isEnabled = isNameValid && isCardValid && isDateValid && isCvcValid
    }

    // LÓGICA COMPATIBLE CON API 24
    private fun isFechaValida(fechaText: String): Boolean {
        return try {
            val parts = fechaText.split("/")
            if (parts.size != 2) return false

            val inputMes = parts[0].toInt()
            val inputAnio = parts[1].toInt() + 2000

            if (inputMes !in 1..12) return false

            val calendar = Calendar.getInstance()
            val actualAnio = calendar.get(Calendar.YEAR)
            val actualMes = calendar.get(Calendar.MONTH) + 1 // Calendar meses son 0-11

            // Comparamos años primero
            if (inputAnio > actualAnio) return true
            if (inputAnio == actualAnio && inputMes >= actualMes) return true

            false
        } catch (e: Exception) {
            false
        }
    }
}