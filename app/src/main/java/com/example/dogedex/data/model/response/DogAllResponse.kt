package com.example.dogedex.data.model.response

import com.squareup.moshi.Json

class DogAllResponse(
    val message: String = "",
    @field:Json(name = "is_success") val isSuccess:Boolean = false,
    val data: DogListResponse,
)
