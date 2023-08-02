package com.example.payruptask.network

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHandler {

    val retrofit: ApiInterface = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
        .create(ApiInterface::class.java)

}
