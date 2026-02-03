package com.grupo5.tickets4u

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo5.tickets4u.login.AuthRetrofitClient
import com.grupo5.tickets4u.login.RegisterRequest
import com.grupo5.tickets4u.login.RegisterResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etRegName)
        val etEmail = findViewById<EditText>(R.id.etRegEmail)
        val etPassword = findViewById<EditText>(R.id.etRegPassword)
        val etPasswordConfirm = findViewById<EditText>(R.id.etRegPasswordConfirm)

        val tvNameError = findViewById<TextView>(R.id.tvRegNameError)
        val tvEmailError = findViewById<TextView>(R.id.tvRegEmailError)
        val tvPasswordError = findViewById<TextView>(R.id.tvRegPasswordError)
        val tvPasswordConfirmError = findViewById<TextView>(R.id.tvRegPasswordConfirmError)

        val btnDoRegister = findViewById<Button>(R.id.btnDoRegister)
        val btnGoLogin = findViewById<Button>(R.id.btnGoLogin)

        btnDoRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etPasswordConfirm.text.toString()

            tvNameError.visibility = View.GONE
            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE
            tvPasswordConfirmError.visibility = View.GONE

            var isValid = true

            if (name.isEmpty()) {
                tvNameError.text = "Introduce tu nombre de usuario"
                tvNameError.visibility = View.VISIBLE
                isValid = false
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvEmailError.text = "El email no es válido"
                tvEmailError.visibility = View.VISIBLE
                isValid = false
            }

            if (!isValidPassword(password)) {
                tvPasswordError.text = "La contraseña debe tener 1 MAYÚSCULA, 1 símbolo y al menos 6 números"
                tvPasswordError.visibility = View.VISIBLE
                isValid = false
            }

            if (password != confirmPassword) {
                tvPasswordConfirmError.text = "Las contraseñas no coinciden"
                tvPasswordConfirmError.visibility = View.VISIBLE
                isValid = false
            }

            if (isValid) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response: RegisterResponse = AuthRetrofitClient.instance.register(
                            RegisterRequest(name, email, password)
                        )

                        val prefs = getSharedPreferences("TICKETS4U_PREFS", Context.MODE_PRIVATE)
                        prefs.edit().apply {
                            putLong("USER_ID", response.id)
                            putString("USER_NAME", response.nombreUsuario)
                            putString("USER_EMAIL", response.email)
                            apply()
                        }

                        runOnUiThread {
                            Toast.makeText(this@RegisterActivity, "Registro correcto", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        }

                    } catch (e: HttpException) {
                        runOnUiThread {
                            tvEmailError.text = "Email ya registrado o error en el registro"
                            tvEmailError.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        btnGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val regex = Regex("""^(?=(?:.*\d){6,})(?=.*[A-Z])(?=.*[^A-Za-z0-9]).+$""")
        return regex.matches(password)
    }
}
