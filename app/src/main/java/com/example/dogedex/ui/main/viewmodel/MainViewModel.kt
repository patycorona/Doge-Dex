package com.example.dogedex.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.domain.usecase.DogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    var dogUseCase: DogUseCase
    ) :ViewModel() {

    val dog : MutableLiveData<DogModel> by lazy {
        MutableLiveData<DogModel>()
    }

    fun getDogByMlid(mlDogId: String){
        viewModelScope.launch {
            dog.value = dogUseCase.getDogByMlid(mlDogId)
        }
    }
}