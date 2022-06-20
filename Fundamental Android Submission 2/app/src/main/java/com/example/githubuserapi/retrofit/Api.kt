package com.example.githubuserapi.retrofit

import com.example.githubuserapi.model.User
import com.example.githubuserapi.model.ResponseUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api  {

    @GET("users")
    fun getApi() : Call<List<User>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(@Query("q") query : String?) : Call<ResponseUser>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username : String? ) : Call<User>

    @GET("users/{username}/{follow}")
    @Headers("Authorization: token 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")
    fun getDetailFollow(@Path("username") username: String, @Path("follow") follow : String) : Call<ArrayList<User>>
}