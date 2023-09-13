package com.example.dogedex.domain.usecase

import com.example.dogedex.data.model.request.DogToUserRequest
import com.example.dogedex.data.model.response.DefaultResponse
import com.example.dogedex.data.repository.DogRepository
import com.example.dogedex.domain.model.DogModel
import javax.inject.Inject

class DogUseCase @Inject constructor(var dogRepository: DogRepository) {
    suspend fun getAllDogs():List<DogModel> = dogRepository.getAllDogs()
    suspend fun addDogToUser(dogToUserRequest: DogToUserRequest): DefaultResponse =
        dogRepository.addDogToUser(dogToUserRequest)

    suspend fun getDogCollection():List<DogModel> = dogRepository.getDogCollection()

    suspend fun getDogByMlid(mlDogId: String):DogModel = dogRepository.getDogByMlid(mlDogId)
}