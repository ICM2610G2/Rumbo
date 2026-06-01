
package com.appnotresponding.rumbo.models


import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.google.firebase.storage.FirebaseStorage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

data class RegisterState(
    val name: String = "",
    val lastname: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val photoUri: Uri? = null,
    val isLoading: Boolean = false,
    val firebaseError: String = ""
)

class RegisterViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private var encryptedPrefs: android.content.SharedPreferences? = null

    private fun initPrefs(context: Context) {
        if (encryptedPrefs != null) return
        val appContext = context.applicationContext
        try {
            val masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            encryptedPrefs = EncryptedSharedPreferences.create(
                appContext,
                "rumbo_secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            try {
                appContext.deleteSharedPreferences("rumbo_secure_prefs")
                val masterKey = MasterKey.Builder(appContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

                encryptedPrefs = EncryptedSharedPreferences.create(
                    appContext,
                    "rumbo_secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (ex: Exception) {
                encryptedPrefs = appContext.getSharedPreferences("rumbo_fallback_prefs", Context.MODE_PRIVATE)
            }
        }
    }

    private fun saveCredentials(email: String, password: String) {
        encryptedPrefs?.edit()
            ?.putString("email", email)
            ?.putString("password", password)
            ?.commit()
    }

    fun updateName(name: String) {
        _registerState.update { it.copy(name = name) }
    }

    fun updateLastname(lastname: String) {
        _registerState.update { it.copy(lastname = lastname) }
    }

    fun updatePhone(phone: String) {
        _registerState.update { it.copy(phone = phone) }
    }

    fun updateEmail(email: String) {
        _registerState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _registerState.update { it.copy(password = password) }
    }

    fun updatePhoto(uri: Uri?) {
        _registerState.update { it.copy(photoUri = uri) }
    }

    fun isFormValid(): Boolean {
        val state = _registerState.value
        val emailRegex = Regex("""^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$""")
        val phoneRegex = Regex("^\\+\\d{10,14}$")
        val passwordRegex = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$""")

        return state.name.isNotBlank() &&
                state.lastname.isNotBlank() &&
                phoneRegex.matches(state.phone) &&
                emailRegex.matches(state.email) &&
                passwordRegex.matches(state.password)
    }

    fun register(context: Context, onSuccess: () -> Unit) {
        val state = _registerState.value
        if (!isFormValid()) return

        initPrefs(context)
        _registerState.update { it.copy(isLoading = true, firebaseError = "") }

        auth.createUserWithEmailAndPassword(state.email, state.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        if (state.photoUri != null) {
                            val storageRef = FirebaseStorage.getInstance("gs://rumbowapp.firebasestorage.app")
                                .getReference("profile_pictures/$userId.jpg")
                            storageRef.putFile(state.photoUri)
                                .addOnSuccessListener {
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        saveUserToDatabase(userId, downloadUrl.toString(), context, onSuccess)
                                    }.addOnFailureListener { e ->
                                        _registerState.update {
                                            it.copy(isLoading = false, firebaseError = e.message ?: "Error al obtener URL de descarga")
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    _registerState.update {
                                        it.copy(isLoading = false, firebaseError = e.message ?: "Error al subir imagen")
                                    }
                                }
                        } else {
                            saveUserToDatabase(userId, null, context, onSuccess)
                        }
                    } else {
                        _registerState.update { it.copy(isLoading = false, firebaseError = "Error al obtener ID de usuario") }
                    }
                } else {
                    _registerState.update {
                        it.copy(isLoading = false, firebaseError = task.exception?.message ?: "Error al registrar usuario")
                    }
                }
            }
    }

    private fun saveUserToDatabase(userId: String, photoUrl: String?, context: Context, onSuccess: () -> Unit) {
        val state = _registerState.value
        val newUser = User(
            id = userId,
            name = state.name,
            lastname = state.lastname,
            email = state.email,
            phone = state.phone,
            profilePictureUrl = photoUrl
        )
        dbRef.child(userId).setValue(newUser)
            .addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    auth.signInWithEmailAndPassword(state.email, state.password)
                        .addOnCompleteListener { signInTask ->
                            _registerState.update { it.copy(isLoading = false) }
                            if (signInTask.isSuccessful) {
                                saveCredentials(state.email, state.password)
                                onSuccess()
                            } else {
                                _registerState.update {
                                    it.copy(firebaseError = signInTask.exception?.message ?: "Error al iniciar sesión")
                                }
                            }
                        }
                } else {
                    _registerState.update {
                        it.copy(isLoading = false, firebaseError = dbTask.exception?.message ?: "Error al guardar en base de datos")
                    }
                }
            }
    }
}
