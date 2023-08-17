package com.example.dogedex.data.repository

import com.example.dogedex.data.model.request.LoginRequest
import com.example.dogedex.data.model.request.UserRequest
import com.example.dogedex.data.network.CoreHomeAPI
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.model.ConstantGeneral.Companion.EMAIL
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ID
import com.example.dogedex.domain.model.ConstantGeneral.Companion.TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(var coreHomeApi: CoreHomeAPI) : AuthRepository {

    override suspend fun userRegister(userRequest: UserRequest): AuthModel {
        return withContext(Dispatchers.IO) {
            AuthModel(ID.toLong(), EMAIL, TOKEN) //coreHomeApi.login(loginRequest
        }
    }

    override suspend fun login(loginRequest: LoginRequest): AuthModel {
        return withContext(Dispatchers.IO) {
            AuthModel(ID.toLong(), EMAIL, TOKEN)//coreHomeApi.login(loginRequest
        }
    }

}