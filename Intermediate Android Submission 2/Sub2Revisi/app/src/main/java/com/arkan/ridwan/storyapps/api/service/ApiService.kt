package com.arkan.ridwan.storyapps.api.service

import com.arkan.ridwan.storyapps.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(
        @Body loginUser: LoginUser
    ): Call<LoginResponse>

    @POST("register")
    fun register(
        @Body registerUser: RegisterUser
    ): Call<RegisterResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody
    ): Call<StoryResponse>
    @GET("stories")
    fun getStoriesLoc(
        @Header("Authorization") auth: String,
        @Query("location") loc : Int = 1
    ): Call<StoryResponseMaps>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StoriesResponse>

}