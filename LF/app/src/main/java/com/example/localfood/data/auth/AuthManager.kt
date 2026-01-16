package com.example.localfood.data.auth

import android.content.Context
import com.google.gson.Gson

data class UserData(val id: Int, val email: String, val nome: String)

class AuthManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("localfood_auth", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveLogin(email: String, nome: String = "", userId: Int = 0) {
        prefs.edit()
            .putString("email", email)
            .putString("nome", nome)
            .putInt("userId", userId)
            .putBoolean("isLoggedIn", true)
            .apply()
    }

    fun saveUser(user: UserData) {
        val userJson = gson.toJson(user)
        prefs.edit()
            .putString("userData", userJson)
            .putBoolean("isLoggedIn", true)
            .apply()
    }

    fun getUserData(): UserData? {
        val userJson = prefs.getString("userData", null) ?: return null
        return gson.fromJson(userJson, UserData::class.java)
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("isLoggedIn", false)
    fun getEmail(): String = prefs.getString("email", "cliente@teste.com") ?: "cliente@teste.com"
    fun getNome(): String = prefs.getString("nome", "") ?: ""
    fun getUserId(): Int = prefs.getInt("userId", 0)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
