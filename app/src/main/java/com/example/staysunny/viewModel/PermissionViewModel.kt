package com.example.staysunny.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionViewModel: ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _mensaje = MutableLiveData<Boolean>()
    val mensaje: LiveData<Boolean>
        get() = _mensaje

    fun triggerSuccessMessage() {
        _mensaje.value = true
    }

    fun resetSuccessMessage() {
        _mensaje.value = false
    }

    fun showLoader() {
        _loaderState.value = true
    }

    fun hideLoader() {
        _loaderState.value = false
    }
}