package com.grupo5.tickets4u

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.grupo5.tickets4u.login.*
import com.grupo5.tickets4u.Usuario

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvEmailError: TextView
    private lateinit var tvPasswordError: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_1)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvEmailError = findViewById(R.id.tvEmailError)
        tvPasswordError = findViewById(R.id.tvPasswordError)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        setupPasswordToggle()
        setupLoginButton()
        setupRegisterButton()
    }

    private fun setupPasswordToggle() {
        var isPasswordVisible = false
        val eyeOpen = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)
        val eyeClosed = ContextCompat.getDrawable(this, R.drawable.ic_eye_open)

        etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeClosed, null)

        etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etPassword.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (etPassword.right - drawableEnd.bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    etPassword.transformationMethod = if (isPasswordVisible)
                        android.text.method.HideReturnsTransformationMethod.getInstance()
                    else
                        android.text.method.PasswordTransformationMethod.getInstance()

                    etPassword.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, if (isPasswordVisible) eyeOpen else eyeClosed, null
                    )
                    etPassword.setSelection(etPassword.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun setupLoginButton() {
        btnLogin.setOnClickListener {
            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvEmailError.text = "El email no es válido"
                tvEmailError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                tvPasswordError.text = "Introduce la contraseña"
                tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Llamada al backend con coroutines
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.instance.login(LoginRequest(email, password))

                    // Guardar token y usuario en SharedPreferences
                    val prefs = getSharedPreferences("TICKETS4U_PREFS", Context.MODE_PRIVATE)
                    prefs.edit().apply {
                        putString("TOKEN", response.token)
                        putLong("USER_ID", response.usuario.id)
                        putString("USER_NAME", response.usuario.nombreUsuario)
                        putString("USER_EMAIL", response.usuario.email)
                        putString("USER_ROLE", response.usuario.rol.name)
                        apply()
                    }

                    // Ir a MainActivity en el hilo principal
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login correcto", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } catch (e: HttpException) {
                    runOnUiThread {
                        tvPasswordError.text = "Email o contraseña incorrectos"
                        tvPasswordError.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Log.e("LOGIN_ERROR", e.message ?: "Error")
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupRegisterButton() {
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
