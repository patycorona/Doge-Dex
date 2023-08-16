package com.example.dogedex.data.network

import com.example.dogedex.data.model.request.LoginRequest
import com.example.dogedex.data.model.request.UserRequest
import com.example.dogedex.data.model.response.AuthResponse
import com.example.dogedex.data.model.response.DogAllResponse
import com.example.dogedex.data.model.response.UserRegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface CoreHomeAPI {

    @GET("dogs")
    @Headers("Content-Type: application/json ")
    suspend fun getAllDogs(): DogAllResponse

    @POST("sign_up")
    @Headers("Content-Type: application/json ")
    suspend fun userRegister(@Body userRequest: UserRequest): AuthResponse

    @POST("sign_in")
    @Headers("Content-Type: application/json ")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

}


