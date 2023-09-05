package com.example.dogedex.domain.usecase

import androidx.camera.core.ImageProxy
import com.example.dogedex.data.repository.ClassiFierRepository
import com.example.dogedex.domain.model.DogRecognition
import com.example.dogedex.ui.machinelearning.ClassiFier
import javax.inject.Inject

class ClassiFierUseCase @Inject constructor(var classiFierRepository: ClassiFierRepository) {

    suspend fun recognizeImage(imageProxy: ImageProxy , classiFier: ClassiFier): DogRecognition =
        classiFierRepository.recognizeImage(imageProxy, classiFier)
}