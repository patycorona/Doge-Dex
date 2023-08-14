package com.example.dogedex.data.network

import com.example.dogedex.data.model.response.DogAllResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface CoreHomeAPI {

    @GET("dogs")
    @Headers("Content-Type: application/json ")
    suspend fun getAllDogs(): DogAllResponse

}