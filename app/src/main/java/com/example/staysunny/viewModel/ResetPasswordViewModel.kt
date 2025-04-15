package com.example.staysunny.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData para el estado del loader (si está cargando o no)
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    // LiveData para el estado de éxito o error al intentar enviar el correo de recuperación
    private val _passwordResetState = MutableLiveData<Boolean>()
    val passwordResetState: LiveData<Boolean> get() = _passwordResetState

    // Función para enviar el correo de restablecimiento de contraseña
    fun sendPasswordResetEmail(email: String) {
        _loaderState.postValue(true)  // Mostrar el loader (cargando)

        // Intentar enviar el correo de restablecimiento
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _loaderState.postValue(false)  // Ocultar el loader al completar la tarea

                // Si la tarea fue exitosa, indicar éxito (true) o error (false)
                _passwordResetState.postValue(task.isSuccessful)
            }
            .addOnFailureListener {
                _loaderState.postValue(false)  // Si falla, ocultar el loader
                _passwordResetState.postValue(false)  // Indicar fallo
            }
    }

}
