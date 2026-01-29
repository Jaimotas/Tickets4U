package com.grupo5.tickets4u

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

data class Usuario(val email: String, val password: String)

class LoginActivity : AppCompatActivity() {

    // Usuarios registrados de ejemplo
    private val usuariosRegistrados = listOf(
        Usuario("user1@gmail.com", "Aa!111111"),
        Usuario("user2@gmail.com", "Bb@222222"),
        Usuario("user3@yahoo.es", "Cc#333333")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_1)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvEmailError = findViewById<TextView>(R.id.tvEmailError)
        val tvPasswordError = findViewById<TextView>(R.id.tvPasswordError)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // ---------- OJO MOSTRAR / OCULTAR CONTRASEÑA ----------
        var isPasswordVisible = false
        val eyeOpen = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)
        val eyeClosed = ContextCompat.getDrawable(this, R.drawable.ic_eye_open)

        // Icono inicial: ojo cerrado
        etPassword.setCompoundDrawablesWithIntrinsicBounds(
            null, null, eyeClosed, null
        )

        etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etPassword.compoundDrawables[2] // derecha
                if (drawableEnd != null &&
                    event.rawX >= (etPassword.right - drawableEnd.bounds.width())
                ) {
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        etPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, eyeOpen, null
                        )
                    } else {
                        etPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, eyeClosed, null
                        )
                    }
                    etPassword.setSelection(etPassword.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }
        // ------------------------------------------------------

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE

            var isValid = true

            // 1) Validar formato básico de email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvEmailError.text = "El email no es válido"
                tvEmailError.visibility = View.VISIBLE
                isValid = false
            }

            // 2) Comprobar que email y contraseña estén en la lista de usuarios registrados
            if (isValid) {
                val usuario =
                    usuariosRegistrados.find { it.email == email && it.password == password }

                if (usuario == null) {
                    tvPasswordError.text = "Email o contraseña incorrectos"
                    tvPasswordError.visibility = View.VISIBLE
                } else {
                    // LOGIN CORRECTO -> pantalla de carga y luego menú
                    val loadingIntent = Intent(this, LoadingActivity::class.java)
                    startActivity(loadingIntent)

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)
                }
            }
        }

        // Botón para ir a la pantalla de registro
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}