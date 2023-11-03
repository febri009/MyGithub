package com.example.core.data.source.remote.network

sealed class ApiResponse<out R> {
    data class MySuccess<out T>(val data: T) : ApiResponse<T>()
    data class MyError(val errorMessage: String) : ApiResponse<Nothing>()
    data object MyEmpty : ApiResponse<Nothing>()
}