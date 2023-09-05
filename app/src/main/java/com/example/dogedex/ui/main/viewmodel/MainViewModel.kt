package com.example.dogedex.ui.main.viewmodel

import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.domain.model.DogRecognition
import com.example.dogedex.domain.usecase.ClassiFierUseCase
import com.example.dogedex.domain.usecase.DogUseCase
import com.example.dogedex.ui.machinelearning.ClassiFier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    var dogUseCase: DogUseCase,
    var classiFierUseCase: ClassiFierUseCase
    ) :ViewModel() {

    val dog : MutableLiveData<DogModel> by lazy {
        MutableLiveData<DogModel>()
    }

    val dog_Recognition : MutableLiveData<DogRecognition> by lazy {
        MutableLiveData<DogRecognition>()
    }

    fun recognizeImage(imageProxy: ImageProxy, classfier: ClassiFier){
        viewModelScope.launch {
            dog_Recognition.value = classiFierUseCase.recognizeImage(imageProxy, classfier)
            imageProxy.close()
        }
    }

    fun getDogByMlid(mlDogId: String){
        viewModelScope.launch {
            dog.value = dogUseCase.getDogByMlid(mlDogId)
        }
    }
}