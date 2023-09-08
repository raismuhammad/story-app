package com.raisproject.storyapp.data

sealed class Result<out R> private constructor(){
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val message: String? = null): Result<Nothing>()
    object Loading: Result<Nothing>()
}