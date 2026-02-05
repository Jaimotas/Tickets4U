package com.grupo5.tickets4u

import android.content.Context

object SessionManager {

    private const val PREFS = "TICKETS4U_PREFS"

    fun getUserId(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getLong("USER_ID", -1L)
    }

    fun getUserEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString("USER_EMAIL", null)
    }

    fun isLogged(context: Context): Boolean {
        return getUserId(context) != -1L
    }

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
