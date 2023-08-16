package com.example.dogedex.data.repository

import com.example.dogedex.data.network.CoreHomeAPI
import com.example.dogedex.domain.model.DogModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(var coreHomeApi: CoreHomeAPI) : DogRepository {
    override suspend fun getAllDogs(): MutableList<DogModel> {
        return withContext(Dispatchers.IO) {
            val dogListApiResponse = coreHomeApi.getAllDogs()
            dogListApiResponse.data.dogs
        }
    }
}