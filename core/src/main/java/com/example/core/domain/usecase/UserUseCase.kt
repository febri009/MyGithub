package com.example.core.domain.usecase

import com.example.core.domain.model.Users
import kotlinx.coroutines.flow.Flow

interface UserUseCase {

    fun gettingUsersByUsername(username: String): Flow<com.example.core.data.Resource<List<Users>>>

    fun gettingUserDetail(username: String): Flow<com.example.core.data.Resource<Users>>

    fun gettingUserFollowers(username: String): Flow<com.example.core.data.Resource<List<Users>>>

    fun gettingUserFollowing(username: String): Flow<com.example.core.data.Resource<List<Users>>>

    fun gettingAllUserFavorite(): Flow<List<Users>>

    suspend fun insertUserFavorite(users: Users)

    suspend fun deleteUserFavorite(users: Users)

    fun getFavoriteIsExists(username: String): Flow<Boolean>

    fun gettingThemeSetting(): Flow<Boolean>

    suspend fun saveThemeSetup(isDarkModeActive: Boolean)
}