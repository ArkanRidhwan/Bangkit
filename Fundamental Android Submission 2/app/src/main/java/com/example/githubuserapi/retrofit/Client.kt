package com.example.githubuserapi.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    companion object{

        fun getApiServices() : Api {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(Api::class.java)

        }
    }
}