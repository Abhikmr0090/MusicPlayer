package com.example.payruptask.network

import retrofit2.http.GET


const val baseURL = "https://jsonplaceholder.typicode.com"

interface ApiInterface {

    @GET("/users")
    suspend fun getContacts() : ArrayList<UsersModel>

}