package com.example.dogedex.data.repository

import androidx.camera.core.ImageProxy
import com.example.dogedex.domain.model.DogRecognition
import com.example.dogedex.ui.machinelearning.ClassiFier

interface ClassiFierRepository{
    suspend fun recognizeImage(imageProxy: ImageProxy, classiFier: ClassiFier): DogRecognition
}