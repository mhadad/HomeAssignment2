package com.mf.homeassignment2.domain.models

sealed class UIStates<out T> {
    object Loading: UIStates<Nothing>()
    class Success<T>(val data: T): UIStates<T>()
    class Error(val message: String): UIStates<Nothing>()
}