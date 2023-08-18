package com.example.dogedex.data.model.request

import com.squareup.moshi.Json

class UserRequest (
    var email: String = "",
    var password: String = "",
    @field:Json(name = "password_confirmation") var passwordConfirm : String = ""
)