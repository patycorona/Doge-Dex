package com.example.dogedex.domain.usecase

import com.example.dogedex.data.repository.DogRepository
import com.example.dogedex.domain.model.DogModel
import javax.inject.Inject

class DogUseCase @Inject constructor(var dogRepository: DogRepository) {

    suspend fun getAllDogs():MutableList<DogModel> = dogRepository.getAllDogs()

}