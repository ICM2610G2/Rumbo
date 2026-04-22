package com.appnotresponding.rumbo.models

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences //TOFIX
import androidx.security.crypto.MasterKey  //TOFIX
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

// https://kotlinlang.org/docs/sealed-classes.html
sealed class AuthResult {
    object Idle : AuthResult()
    object Loading : AuthResult()
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val authResult: AuthResult = AuthResult.Idle,
    val hasBiometricCredentials: Boolean = false,
)

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private var encryptedPrefs: android.content.SharedPreferences? = null

    //EncryptedSharedPreferences:https://developer.android.com/reference/androidx/security/crypto/EncryptedSharedPreferences
    //MasterKey: https://developer.android.com/reference/androidx/security/crypto/MasterKey
    fun initPrefs(context: Context) {
        if (encryptedPrefs != null) return
        val masterKey =
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            "rumbo_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        // Al inicializar, revisamos si ya hay credenciales guardadas
        _loginState.update { it.copy(hasBiometricCredentials = hasCredentials()) }
    }

    fun updateEmail(newValue: String) {
        _loginState.update { it.copy(email = newValue) }
    }

    fun updatePassword(newValue: String) {
        _loginState.update { it.copy(password = newValue) }
    }

    fun loginWithEmailPassword() {
        val email = _loginState.value.email
        val password = _loginState.value.password
        if (email.isBlank() || password.isBlank()) return

        _loginState.update { it.copy(authResult = AuthResult.Loading) }

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                saveCredentials(email, password)
                _loginState.update { it.copy(authResult = AuthResult.Success) }
            }.addOnFailureListener { e ->
                _loginState.update { it.copy(authResult = AuthResult.Error(e.message ?: "Error")) }
            }
    }

    // BiometricPrompt: https://developer.android.com/training/sign-in/biometric-auth
    // BiometricManager: https://developer.android.com/reference/androidx/biometric/BiometricManager
    fun loginWithBiometric(activity: FragmentActivity) {

        // https://developer.android.com/reference/androidx/biometric/BiometricManager#canAuthenticate(int)
        val canAuth = BiometricManager.from(activity)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)

        if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
            _loginState.update { it.copy(authResult = AuthResult.Error("Biometría no disponible")) }
            return
        }

        //ContextCompat.getMainExecutor: https://developer.android.com/reference/androidx/core/content/ContextCompat#getMainExecutor(android.content.Context)
        val executor = ContextCompat.getMainExecutor(activity)


        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                val creds = getCredentials()
                if (creds != null) {
                    firebaseSignIn(creds.first, creds.second)
                } else {
                    _loginState.update { it.copy(authResult = AuthResult.Error("No hay credenciales guardadas")) }
                }
            }

            //  https://developer.android.com/reference/androidx/biometric/BiometricPrompt#ERROR_NEGATIVE_BUTTON()
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                    _loginState.update { it.copy(authResult = AuthResult.Idle) }
                } else {
                    _loginState.update { it.copy(authResult = AuthResult.Error(errString.toString())) }
                }
            }

            override fun onAuthenticationFailed() {}
        }

        // https://developer.android.com/reference/androidx/biometric/BiometricPrompt.PromptInfo.Builder
        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Acceder a Rumbo")
            .setSubtitle("Confirma tu identidad").setNegativeButtonText("Usar contraseña").build()

        BiometricPrompt(activity, executor, callback).authenticate(promptInfo)
    }

    private fun firebaseSignIn(email: String, password: String) {
        _loginState.update { it.copy(authResult = AuthResult.Loading) }
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                _loginState.update { it.copy(authResult = AuthResult.Success) }
            }.addOnFailureListener { e ->
                clearCredentials()
                _loginState.update {
                    it.copy(
                        hasBiometricCredentials = false,
                        authResult = AuthResult.Error("Sesión expirada, ingresa tu contraseña")
                    )
                }
            }
    }

    //https://developer.android.com/reference/android/content/SharedPreferences.Editor
    private fun saveCredentials(email: String, password: String) {
        encryptedPrefs?.edit()?.putString("email", email)?.putString("password", password)?.apply()
        _loginState.update { it.copy(hasBiometricCredentials = true) }
    }

    private fun getCredentials(): Pair<String, String>? {
        val email = encryptedPrefs?.getString("email", null)
        val password = encryptedPrefs?.getString("password", null)
        return if (email != null && password != null) Pair(email, password) else null
    }

    private fun hasCredentials() = encryptedPrefs?.contains("email") == true

    private fun clearCredentials() {
        encryptedPrefs?.edit()?.clear()?.apply()
    }
}