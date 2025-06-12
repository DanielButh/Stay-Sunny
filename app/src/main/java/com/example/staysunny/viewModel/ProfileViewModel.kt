package com.example.staysunny.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staysunny.core.ResultWrapper
import com.example.staysunny.model.User
import com.example.staysunny.network.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState
    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = _userInfo
    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean>
        get() = _operationSuccess

    fun getUserInfo() {
        _loaderState.value = true
        viewModelScope.launch {
            when (val result = repository.getUser()) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _userInfo.value = result.data
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    val errorMessage = result.exception.message
                }
            }
        }
    }

    fun updateUserInfo(name: String, lastName: String, userName: String, bornDate: java.util.Date) {
        _loaderState.value = true
        val userId = firebaseAuth.currentUser?.uid ?: ""
        val user = User(userId, name, lastName, userName, bornDate)
        viewModelScope.launch {
            when (val result = repository.updateUser(user)) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _operationSuccess.value = true
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    val errorMessage = result.exception.message
                }
            }
        }
    }
}