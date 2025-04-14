package com.example.staysunny.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel: ViewModel() {
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _createdUser = MutableLiveData<Boolean>()
    val createdUser: LiveData<Boolean>
        get() = _createdUser

    private val firebase = FirebaseAuth.getInstance()

    fun requestRegister(email: String, password: String) {
        _loaderState.value = true
        _createdUser.value = false

        viewModelScope.launch {
            val result = firebase.createUserWithEmailAndPassword(email, password).await()
            _loaderState.value = false

            result.user?.let {
                Log.i("Firebase", "Se puedo crear el usuario")
                _createdUser.value = true
            } ?: run {
                Log.e("Firebase", "Ocurrió un problema")
            }
        }
    }
}