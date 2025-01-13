package com.cristiangoncas.greenhousemonitor.domain.models

sealed interface CustomResult<out T> {
    data class Success<T>(val data: T) : CustomResult<T>
    data class Error(val message: String) : CustomResult<Nothing>
    data object Loading : CustomResult<Nothing>

    fun isSuccessful(): Boolean {
        return this is Success<*>
    }
}
