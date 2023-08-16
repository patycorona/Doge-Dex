package com.example.dogedex.data.model.response

import com.squareup.moshi.Json

class UserRegisterResponse (
    val message : String,
    @field:Json(name = "is_success") val isSuccess:String,
    val data: UserResponse
)