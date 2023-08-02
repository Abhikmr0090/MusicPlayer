package com.example.payruptask.network

sealed class NetworkStates<out T> {
    object Loading                        : NetworkStates<Nothing>()
    data class  Success<T>(var data : T)  : NetworkStates<T>()
    data class  Error(var error : String) : NetworkStates<Nothing>()
}