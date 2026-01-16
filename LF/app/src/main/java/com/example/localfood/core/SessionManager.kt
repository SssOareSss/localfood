package com.example.localfood.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionManager {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    fun saveSession(token: String?, userId: String?) {
        _token.value = token
        _userId.value = userId
    }

    fun clearSession() {
        _token.value = null
        _userId.value = null
    }

    fun isLoggedIn(): Boolean = _token.value != null
}
