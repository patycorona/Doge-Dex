package com.example.dogedex.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.data.model.request.LoginRequest
import com.example.dogedex.data.model.request.UserRequest
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
):ViewModel() {

    private val _auth = MutableLiveData<AuthModel>()
    val auth: LiveData<AuthModel>
        get() = _auth

    fun userRegister(email: String, pwd:String, pwdConfirm:String){
        val userRequest = UserRequest(email,pwd,pwdConfirm)

        viewModelScope.launch {
            _auth.value = authUseCase.userRegister(userRequest)
        }
    }

    fun login(email: String, pwd:String){
        val loginRequest =  LoginRequest(email,pwd)
        viewModelScope.launch {
            _auth.value = authUseCase.login(loginRequest)
        }
    }
}