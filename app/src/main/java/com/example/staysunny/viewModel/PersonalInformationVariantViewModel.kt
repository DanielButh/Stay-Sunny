package com.example.staysunny.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staysunny.core.ResultWrapper
import com.example.staysunny.model.User
import com.example.staysunny.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PersonalInformationVariantViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> get() = _operationSuccess

    fun createUserInfo(
        userId: String,
        name: String,
        lastName: String,
        userName: String,
        bornDate: Date
    ) {
        _loaderState.value = true

        val user = User(
            id = userId,
            name = name,
            lastName = lastName,
            userName = userName,
            bornDate = bornDate
        )

        viewModelScope.launch {
            when (val result = repository.createUser(user)) {
                is ResultWrapper.Success -> {
                    Log.d("ViewModel", "Usuario creado exitosamente")
                    _operationSuccess.value = true
                }
                is ResultWrapper.Error -> {
                    Log.e("ViewModel", "Error al crear usuario: ${result.exception.message}")
                    _operationSuccess.value = false
                }
            }
            _loaderState.value = false
        }
    }
}

