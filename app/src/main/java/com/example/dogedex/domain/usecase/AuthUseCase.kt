package com.example.dogedex.domain.usecase

import com.example.dogedex.data.model.request.LoginRequest
import com.example.dogedex.data.model.request.UserRequest
import com.example.dogedex.data.repository.AuthRepository
import com.example.dogedex.domain.model.AuthModel
import javax.inject.Inject

class AuthUseCase @Inject constructor(var authRepository: AuthRepository) {

    suspend fun userRegister(loginRequest: UserRequest): AuthModel = authRepository.userRegister(loginRequest)

    suspend fun login(loginRequest: LoginRequest): AuthModel = authRepository.login(loginRequest)
}