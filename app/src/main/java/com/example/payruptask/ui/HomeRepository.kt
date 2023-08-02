package com.example.payruptask.ui

import com.example.payruptask.network.NetworkStates
import com.example.payruptask.network.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomeRepository(private val apiInterface: ApiInterface) {

    suspend fun getContacts() = safeApiCall {
        apiInterface.getContacts()
    }

}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkStates<T> {
    return withContext(Dispatchers.IO) {
        try {
            NetworkStates.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    NetworkStates.Error("An Error Occurred")
                }

                else -> {
                    NetworkStates.Error(throwable.message.toString())
                }
            }
        }
    }
}