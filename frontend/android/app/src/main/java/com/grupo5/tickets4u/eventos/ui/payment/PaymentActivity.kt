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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo5.tickets4u.R

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

        val total = intent.getDoubleExtra("TOTAL_CARRITO", 0.0)
        txtTotalPago.text = "Total a pagar: %.2f€".format(total)

        // Callback para quitar menús
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
            editText.isCursorVisible = true
        }

        // Formatear número de tarjeta
        edtNumeroTarjeta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().replace(" ", "")
                val formatted = text.chunked(4).joinToString(" ")
                edtNumeroTarjeta.removeTextChangedListener(this)
                edtNumeroTarjeta.setText(formatted)
                edtNumeroTarjeta.setSelection(formatted.length)
                edtNumeroTarjeta.addTextChangedListener(this)
            }
        })

        // Formatear fecha MM/AA
        edtFecha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.length == 2 && !text.contains("/")) {
                    edtFecha.setText("${text.substring(0, 2)}/")
                    edtFecha.setSelection(3)
                }
            }
        })

        // Validar campos
        val fields = listOf(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc)
        fields.forEach { field ->
            field.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    btnConfirmarPago.isEnabled = isFormValid(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc)
                }
            })
        }

        // ✅ REDIRECCIONA A ACTIVITY DE ÉXITO
        btnConfirmarPago.setOnClickListener {
            if (isFormValid(edtTitular, edtNumeroTarjeta, edtFecha, edtCvc)) {
                val intent = Intent(this, PaymentSuccessActivity::class.java)
                intent.putExtra("TOTAL_PAGADO", total)
                startActivity(intent)
                finish()  // Cierra PaymentActivity
            }
        }
    }

    private fun isFormValid(titular: EditText, numero: EditText, fecha: EditText, cvc: EditText): Boolean {
        val titularText = titular.text.toString().trim()
        val numeroText = numero.text.toString().replace(" ", "")
        val fechaText = fecha.text.toString().trim()
        val cvcText = cvc.text.toString()

        return when {
            titularText.isEmpty() -> false
            numeroText.length != 16 -> false
            !fechaText.matches(Regex("^\\d{2}/\\d{2}$")) -> false
            !cvcText.matches(Regex("^\\d{3}$")) -> false
            else -> true
        }
    }
}
