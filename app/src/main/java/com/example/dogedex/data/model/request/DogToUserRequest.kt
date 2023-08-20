package com.example.dogedex.data.model.request

import com.squareup.moshi.Json

class DogToUserRequest (
    @field:Json(name= "dog_id")val dogId:Long
        )