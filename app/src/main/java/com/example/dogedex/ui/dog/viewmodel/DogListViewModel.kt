package com.example.dogedex.ui.dog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.domain.usecase.DogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    var dogUseCase: DogUseCase
    ):ViewModel() {

    val dogList: MutableLiveData<List<DogModel>> by lazy {
        MutableLiveData<List<DogModel>>()
    }

    fun getAllDogs() {
        viewModelScope.launch {
            dogList.value = dogUseCase.getAllDogs()
        }
    }

    fun getdogColection(){
        viewModelScope.launch {
            dogList.value = dogUseCase.getDogCollection()
        }
    }

}