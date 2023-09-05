package com.example.dogedex.ui.dog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.data.model.request.DogToUserRequest
import com.example.dogedex.data.model.response.DefaultResponse
import com.example.dogedex.domain.usecase.DogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogDetailViewModel @Inject constructor( var dogUseCase: DogUseCase):ViewModel(){

    val add_Dog: MutableLiveData<DefaultResponse> by lazy {
        MutableLiveData<DefaultResponse>()
    }

    fun addDogToUser(id :Long){
        val dogToUserRequest = DogToUserRequest(dogId = id)
        viewModelScope.launch {
            add_Dog.value =  dogUseCase.addDogToUser(dogToUserRequest)
        }
    }
}