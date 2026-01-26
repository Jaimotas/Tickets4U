package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etEmail = findViewById<EditText>(R.id.etRegEmail)
        val etPassword = findViewById<EditText>(R.id.etRegPassword)
        val etPasswordConfirm = findViewById<EditText>(R.id.etRegPasswordConfirm)

        val tvEmailError = findViewById<TextView>(R.id.tvRegEmailError)
        val tvPasswordError = findViewById<TextView>(R.id.tvRegPasswordError)
        val tvPasswordConfirmError = findViewById<TextView>(R.id.tvRegPasswordConfirmError)

        val btnDoRegister = findViewById<Button>(R.id.btnDoRegister)
        val btnGoLogin = findViewById<Button>(R.id.btnGoLogin)

        btnDoRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etPasswordConfirm.text.toString()

            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE
            tvPasswordConfirmError.visibility = View.GONE

            var isValid = true

            // 1) Validar email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvEmailError.text = "El email no es válido"
                tvEmailError.visibility = View.VISIBLE
                isValid = false
            }

            // 2) Validar contraseña fuerte
            if (!isValidPassword(password)) {
                tvPasswordError.text =
                    "La contraseña debe tener 1 MAYÚSCULA, 1 símbolo y al menos 6 números"
                tvPasswordError.visibility = View.VISIBLE
                isValid = false
            }

            // 3) Contraseña repetida
            if (password != confirmPassword) {
                tvPasswordConfirmError.text = "Las contraseñas no coinciden"
                tvPasswordConfirmError.visibility = View.VISIBLE
                isValid = false
            }

            if (isValid) {
                // Aquí guardarías el usuario; para el trabajo, vamos al menú
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Volver al login
        btnGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Contraseña: 1 mayúscula, 1 símbolo y al menos 6 dígitos
    private fun isValidPassword(password: String): Boolean {
        val regex = Regex("""^(?=(?:.*\d){6,})(?=.*[A-Z])(?=.*[^A-Za-z0-9]).+$""")
        return regex.matches(password)
    }
}
