package com.example.dogedex.data.model.response

sealed class ResultResponse<out R> {
    data class Success<out T>(val data: T) : ResultResponse<T>()
    data class Error(val exception: Exception) : ResultResponse<Nothing>()
}
