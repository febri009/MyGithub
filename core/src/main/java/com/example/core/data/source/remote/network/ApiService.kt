package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.GithubResponse
import com.example.core.data.source.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun fetchUserByUsername(
        @Query("q") username: String,
    ): GithubResponse

    @GET("users/{username}")
    suspend fun fetchUserDetail(
        @Path("username") username: String,
    ): UserResponse

    @GET("users/{username}/followers")
    suspend fun fetchUserFollowers(
        @Path("username") username: String,
    ): List<UserResponse>

    @GET("users/{username}/following")
    suspend fun fetchUserFollowing(
        @Path("username") username: String,
    ): List<UserResponse>
}