package com.example.dogedex.data.repository

import com.example.dogedex.domain.model.DogModel

interface DogRepository {
    suspend fun getAllDogs():MutableList<DogModel>
}