package com.appnotresponding.rumbo.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class LoginState(
    val email: String = "",
    val password: String = "",
)

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState())
    val loginSate = _loginState.asStateFlow()

    fun updateEmail(newValue: String) {
        _loginState.update { it.copy(email = newValue) }
    }

    fun updatePassword(newValue: String) {
        _loginState.update { it.copy(password = newValue) }
    }
}