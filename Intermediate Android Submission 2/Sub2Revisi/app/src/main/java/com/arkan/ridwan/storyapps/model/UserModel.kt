package com.arkan.ridwan.storyapps.model

import com.google.gson.annotations.SerializedName

data class LoginUser(
    @field:SerializedName("email")
    var email: String,

    @field:SerializedName("password")
    var password: String,
)

data class LoginResponse(
    val loginResult: LoginResult,
    val error: Boolean,
    val message: String
)

data class LoginResult(
    val name: String,
    val userId: String,
    val token: String
)

data class RegisterUser(
    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("email")
    var email: String,

    @field:SerializedName("password")
    var password: String,
)

data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)


