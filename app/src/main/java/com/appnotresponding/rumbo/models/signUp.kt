package com.appnotresponding.rumbo.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterState(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val country: String = "",
)

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState())
    fun updateName(newValue: String) {
        _registerState.update { it.copy(name = newValue) }
    }

    fun updatePhone(newValue: String) {
        _registerState.update { it.copy(phone = newValue) }
    }


    fun updateEmail(newValue: String) {
        _registerState.update { it.copy(email = newValue) }
    }

    fun updatePassword(newValue: String) {
        _registerState.update { it.copy(password = newValue) }
    }

    fun updateCountry(newValue: String) {
        _registerState.update { it.copy(country = newValue) }
    }

    val registerState = _registerState.asStateFlow()

}