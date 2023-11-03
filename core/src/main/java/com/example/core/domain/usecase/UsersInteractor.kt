package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Users
import com.example.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UsersInteractor(
    private val userRepository: IUserRepository,
) : UserUseCase {

    override fun gettingUsersByUsername(username: String): Flow<Resource<List<Users>>> =
        userRepository.fetchUsersByUsername(username)

    override fun gettingUserDetail(username: String): Flow<Resource<Users>> =
        userRepository.fetchUserDetail(username)

    override fun gettingUserFollowers(username: String): Flow<Resource<List<Users>>> =
        userRepository.fetchUserFollowers(username)

    override fun gettingUserFollowing(username: String): Flow<Resource<List<Users>>> =
        userRepository.fetchUserFollowing(username)

    override fun gettingAllUserFavorite(): Flow<List<Users>> =
        userRepository.fetchAllUserFavorite()

    override suspend fun insertUserFavorite(users: Users) =
        userRepository.addUserFavorite(users)

    override suspend fun deleteUserFavorite(users: Users) =
        userRepository.deleteUserFavorite(users)

    override fun getFavoriteIsExists(username: String): Flow<Boolean> =
        userRepository.getFavoriteIsExists(username)

    override fun gettingThemeSetting(): Flow<Boolean> = userRepository.getThemeSetting()

    override suspend fun saveThemeSetup(isDarkModeActive: Boolean) =
        userRepository.saveThemeSetting(isDarkModeActive)

}