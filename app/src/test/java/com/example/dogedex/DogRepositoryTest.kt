package com.example.dogedex

import com.example.dogedex.data.model.request.DogToUserRequest
import com.example.dogedex.data.model.request.LoginRequest
import com.example.dogedex.data.model.request.UserRequest
import com.example.dogedex.data.model.response.AuthResponse
import com.example.dogedex.data.model.response.DefaultResponse
import com.example.dogedex.data.model.response.DogAllResponse
import com.example.dogedex.data.model.response.DogApiResponse
import com.example.dogedex.data.model.response.DogListResponse
import com.example.dogedex.data.model.response.DogResponse
import com.example.dogedex.data.network.CoreHomeAPI
import com.example.dogedex.data.repository.DogRepositoryImpl
import com.example.dogedex.data.repository.mocks.MockRepository
import com.example.dogedex.domain.model.DogModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class DogRepositoryTest {

    @Test
    fun testGetDogColletionSuccess():Unit = runBlocking{
        class FakeCoreAPI: CoreHomeAPI {
            override suspend fun getAllDogs():DogAllResponse {
                val allDogListByUser = mutableListOf<DogModel>()
                allDogListByUser.add(MockRepository.getDogModelMock)
                allDogListByUser.add(MockRepository.getDogModelMock2)
                allDogListByUser.add(MockRepository.getDogModelMock3)


                return DogAllResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        dogs = allDogListByUser
                    )
                )
            }

            override suspend fun userRegister(userRequest: UserRequest): AuthResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginRequest: LoginRequest): AuthResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(dogToUserRequest: DogToUserRequest): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(id: Long): DogAllResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getDogCollection():DogAllResponse {

                val allDogListByUser = mutableListOf<DogModel>()
                allDogListByUser.add(MockRepository.getDogModelMock)
                allDogListByUser.add(MockRepository.getDogModelMock2)

                return DogAllResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        dogs = allDogListByUser
                    )
                )

            }

            override suspend fun getDogByMlid(mlDogId: String): DogApiResponse {
                val dogByMlid = MockRepository.getDogModelMock

                return DogApiResponse(
                    message = "",
                    isSuccess = true,
                    data = DogResponse(
                        dog = dogByMlid
                    )
                )
            }

        }

        val dogRepositoryImpl = DogRepositoryImpl(
            coreHomeApi = FakeCoreAPI()
        )

        val dogColletion = dogRepositoryImpl.getDogCollection()
        assertEquals(2,dogColletion.size)

    }

}