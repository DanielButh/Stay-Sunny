package com.example.staysunny.viewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel:ViewModel() {
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private  val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean>
        get() = _sessionValid

    private val firebase = FirebaseAuth.getInstance()

    fun requestLogin(email: String, password: String){
        _loaderState.value = true
        _sessionValid.value = false

        viewModelScope.launch{
            val result = firebase.signInWithEmailAndPassword(email,password).await()
            _loaderState.value = false

            result.user?.let{
                _sessionValid.value = true
            } ?: run {
                    Log.e("Firebase","Ocurrio un problema")
        }
        }
    }
}