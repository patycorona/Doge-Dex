package com.example.dogedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogModel(
    val id: Long,
    val index: Int,
    val name_es: String,
    val dog_type: String,
    val height_female: String,
    val height_male:String,
    val image_url: String,
    val life_expectancy:String,
    val temperament:String,
    val weight_female:String,
    val weight_male:String
) : Parcelable


