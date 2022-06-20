package submission3final.network

import submission3final.model.response.FollowResponse
import submission3final.model.response.SearchUserResponse
import submission3final.model.response.UserDetailResponse
import submission3final.model.response.UsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getUsers(
        @Header("Authorization") token: String = "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0"
    ): Call<List<UsersResponse>>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<UserDetailResponse>

    @GET("search/users?")
    fun getSearchUser(
        @Query("q") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<SearchUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<List<FollowResponse>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<List<FollowResponse>>
}