package com.example.dogedex.data.repository

import com.example.dogedex.data.model.request.DogToUserRequest
import com.example.dogedex.data.model.response.DefaultResponse
import com.example.dogedex.data.network.CoreHomeAPI
import com.example.dogedex.domain.model.DogModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(var coreHomeApi: CoreHomeAPI) : DogRepository {
    override suspend fun getAllDogs(): List<DogModel> {
        return withContext(Dispatchers.IO) {
            val dogListApiResponse = coreHomeApi.getAllDogs()
            dogListApiResponse.data.dogs
        }
    }

    override suspend fun addDogToUser(dogToUserRequest: DogToUserRequest): DefaultResponse {
        return withContext(Dispatchers.IO) {
            val id = dogToUserRequest.dogId//coreHomeApi.addDogToUser(dogToUserRequest)
            if (id != null) DefaultResponse("Dog Added to list", true,)
            else DefaultResponse("An error has occurred", true,)
        }
    }

    suspend fun getUserDogs(): List<DogModel> {
        return withContext(Dispatchers.IO) {
            val dogListApiResponse = getListDogsByUser() //coreHomeApi.getUserDogs(id)
            dogListApiResponse
        }
    }
    private fun getListDogsByUser() : List<DogModel>{
        var allDogListByUser = mutableListOf<DogModel>()
        allDogListByUser.add(
            DogModel( 8,4, "Pekinés", "Toy","25.3","28.0","https://firebasestorage.googleapis.com/v0/b/perrodex-app.appspot.com/o/dog_details_images%2Fn02086079-pekinese.png?alt=media&token=f3cb4225-6690-42f2-a492-b77fcdeb5ee3",
                "10 - 12","Juguetón, feliz, amistoso","4 kg", "5 kg",false),
        )
        allDogListByUser.add(  DogModel( 16,12, "Beagle", "Toy","25.3","28.0","https://firebasestorage.googleapis.com/v0/b/perrodex-app.appspot.com/o/dog_details_images%2Fn02088364-beagle.png?alt=media&token=7e99c6c2-6c32-4cf1-b9a8-dc0b250f6f58",
            "10 - 12","Juguetón, feliz, amistoso","4 kg", "5 kg",false))

        allDogListByUser.add(  DogModel( 12,8, "Toy Terrier", "Toy","25.3","28.0","https://firebasestorage.googleapis.com/v0/b/perrodex-app.appspot.com/o/dog_details_images%2Fn02087046-toy_terrier.png?alt=media&token=1615339b-1f0a-4eaa-b6bb-64619e96ccc4",
            "10 - 12","Juguetón, feliz, amistoso","4 kg", "5 kg",false))
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

    private fun getCollectionList(allDogList:List<DogModel>,
                                  userDogList:List<DogModel>
    ): List<DogModel> {

        return allDogList.map {
            if(userDogList.contains(it)){
                it
            }else{
                DogModel(it.id,it.index, "","","","",
                    "","","","","", inCollection = false)
            }
        }.sorted()

    }

}