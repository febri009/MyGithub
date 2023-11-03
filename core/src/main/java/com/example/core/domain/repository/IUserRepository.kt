package com.example.core.domain.repository

import com.example.core.data.Resource
import com.example.core.domain.model.Users
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun fetchUsersByUsername(username: String): Flow<Resource<List<Users>>>

    fun fetchUserDetail(username: String): Flow<Resource<Users>>

    fun fetchUserFollowers(username: String): Flow<Resource<List<Users>>>

    fun fetchUserFollowing(username: String): Flow<Resource<List<Users>>>

    fun fetchAllUserFavorite(): Flow<List<Users>>

    suspend fun addUserFavorite(users: Users)

    suspend fun deleteUserFavorite(users: Users)

    fun getFavoriteIsExists(username: String): Flow<Boolean>

    fun getThemeSetting(): Flow<Boolean>

    suspend fun saveThemeSetting(isDarkModeActivity: Boolean)
}