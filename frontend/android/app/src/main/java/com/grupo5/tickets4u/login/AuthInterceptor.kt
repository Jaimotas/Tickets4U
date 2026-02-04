package com.grupo5.tickets4u

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // Lista de rutas que NO necesitan token (rutas públicas)
        val rutasPublicas = listOf(
            "/api/auth/login",
            "/api/auth/register"
        )

        // Si la URL contiene alguna ruta pública, no agregar token
        val esRutaPublica = rutasPublicas.any { url.contains(it) }

        val newRequest = if (esRutaPublica) {
            // No agregar token para rutas públicas
            originalRequest
        } else {
            // Agregar token solo para rutas protegidas
            val prefs = context.getSharedPreferences("TICKETS4U_PREFS", Context.MODE_PRIVATE)
            val token = prefs.getString("TOKEN", null)

            if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }
        }

        return chain.proceed(newRequest)
    }
}