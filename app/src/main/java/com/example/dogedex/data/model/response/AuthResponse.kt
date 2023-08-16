package com.example.dogedex.data.model.response

import com.squareup.moshi.Json

class AuthResponse(
    val id: Long,
    val email:String,
    @field:Json(name = "authentication_token") val token:String
)