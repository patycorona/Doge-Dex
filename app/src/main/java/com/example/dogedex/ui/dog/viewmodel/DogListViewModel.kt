package com.example.dogedex.ui.dog.viewmodel

import androidx.lifecycle.LiveData
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

    private val _dogList = MutableLiveData<MutableList<DogModel>>()
    val dogList: LiveData<MutableList<DogModel>>
        get() = _dogList

    fun getAllDogs()
    {
        viewModelScope.launch {
            _dogList.value = dogUseCase.getAllDogs()
        }
    }
}