package com.example.dogedex.data.repository

import com.example.dogedex.data.model.request.LoginRequest
import com.example.dogedex.data.model.request.UserRequest
import com.example.dogedex.domain.model.AuthModel

interface AuthRepository {

    suspend fun userRegister(userRequest: UserRequest): AuthModel

    suspend fun login(loginRequest: LoginRequest): AuthModel

}