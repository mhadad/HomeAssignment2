package com.mf.homeassignment2.data.models

sealed class DataStates<out T> {
    object Loading: DataStates<Nothing>()
    class Success<T>(val data: T): DataStates<T>()
    class Error(val message: String): DataStates<Nothing>()
}