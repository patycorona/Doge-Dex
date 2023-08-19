package com.example.dogedex.data.repository

import com.example.dogedex.data.model.request.DogToUserRequest
import com.example.dogedex.data.model.response.DefaultResponse
import com.example.dogedex.data.network.CoreHomeAPI
import com.example.dogedex.data.repository.mocks.MockRepository
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ADDED_DOG
import com.example.dogedex.domain.model.ConstantGeneral.Companion.EMPTY
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ERROR_NOT_FOUND
import com.example.dogedex.domain.model.DogModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(private var coreHomeApi: CoreHomeAPI) : DogRepository {
    override suspend fun getAllDogs(): List<DogModel> {
        return withContext(Dispatchers.IO) {
            val dogListApiResponse = coreHomeApi.getAllDogs()
            dogListApiResponse.data.dogs
        }
    }

    override suspend fun addDogToUser(dogToUserRequest: DogToUserRequest): DefaultResponse {
        return withContext(Dispatchers.IO) {
            val id = dogToUserRequest.dogId//coreHomeApi.addDogToUser(dogToUserRequest)
            if (id != null) DefaultResponse(ADDED_DOG, true)
            else DefaultResponse(ERROR_NOT_FOUND, false)
        }
    }

    suspend fun getUserDogs(): List<DogModel> {
        return withContext(Dispatchers.IO) {
            val dogListApiResponse = getListDogsByUser()
            dogListApiResponse
        }
    }
    private fun getListDogsByUser() : List<DogModel>{
        val allDogListByUser = mutableListOf<DogModel>()
        allDogListByUser.add(MockRepository.getDogModelMock)
        allDogListByUser.add(MockRepository.getDogModelMock2)
        allDogListByUser.add(MockRepository.getDogModelMock3)
        return allDogListByUser
    }

    override suspend fun getDogCollection(): List<DogModel> {
       return withContext(Dispatchers.IO) {
           val dogListDeferred = async { getAllDogs() }
           val userDogListDeferred = async { getListDogsByUser()  }

           val listDogResponse = dogListDeferred.await()
           val userDogListResponse = userDogListDeferred.await()

            getCollectionList(listDogResponse , userDogListResponse )
        }
    }

    private fun getCollectionList(allDogList:List<DogModel>, userDogList:List<DogModel>
    ): List<DogModel> {

        return allDogList.map {
            if(userDogList.contains(it)){
                it
            }else{
                DogModel(it.id,it.index, EMPTY,EMPTY,EMPTY,EMPTY,
                    EMPTY,EMPTY,EMPTY,EMPTY,EMPTY, inCollection = false)
            }
        }.sorted()

    }

}