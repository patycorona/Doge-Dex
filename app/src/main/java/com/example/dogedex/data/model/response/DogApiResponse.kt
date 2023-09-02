package com.example.dogedex.data.model.response

import com.example.dogedex.domain.model.DogModel
import com.squareup.moshi.Json

class DogApiResponse (
    val message: String = "",
    @field:Json(name = "is_success") val isSuccess:Boolean = false,
    val data: DogResponse,
    )
