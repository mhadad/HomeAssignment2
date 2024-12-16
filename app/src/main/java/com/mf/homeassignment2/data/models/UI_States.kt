package com.mf.homeassignment2.data.models

sealed class UI_States<out T> {
    class Loading(val isLoading: Boolean): UI_States<Nothing>()
    class Success<T>(val data: T): UI_States<T>()
    class Error(val message: String): UI_States<Nothing>()
}