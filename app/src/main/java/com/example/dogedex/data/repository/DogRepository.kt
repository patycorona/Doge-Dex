package com.example.dogedex.data.repository

import com.example.dogedex.data.model.request.DogToUserRequest
import com.example.dogedex.data.model.response.DefaultResponse
import com.example.dogedex.domain.model.DogModel

interface DogRepository {
    suspend fun getAllDogs():List<DogModel>
    suspend fun addDogToUser(dogToUserRequest: DogToUserRequest): DefaultResponse
    suspend fun getDogCollection():List<DogModel>

}
