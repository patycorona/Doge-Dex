package com.example.dogedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AuthModel (
    val id: Long = 0,
    val email:String = "",
    val authentication_token:String =""
    ): Parcelable


