package com.example.core.data

import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.preferences.SharedPreferences
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.response.UserResponse
import com.example.core.domain.model.Users
import com.example.core.domain.repository.IUserRepository
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val sharedPreferences: SharedPreferences,
) : IUserRepository {

    override fun fetchUsersByUsername(username: String): Flow<Resource<List<Users>>> {
        val networkBoundResource = object : NetworkBoundResource<List<Users>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<Users>> {
                return DataMapper.mapResponsesToUserList(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.fetchUsersByUsername(username)

        }
        return networkBoundResource.asFlow()
    }

    override fun fetchUserDetail(username: String): Flow<Resource<Users>> {
        val networkBoundResource = object : NetworkBoundResource<Users, UserResponse>() {
            override fun loadFromNetwork(data: UserResponse): Flow<Users> {
                return DataMapper.mapResponsesToUserList(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> =
                remoteDataSource.fetchUserDetail(username)

        }
        return networkBoundResource.asFlow()
    }

    override fun fetchUserFollowers(username: String): Flow<Resource<List<Users>>> {
        val networkBoundResource = object : NetworkBoundResource<List<Users>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<Users>> {
                return DataMapper.mapResponsesToUserList(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.fetchUserFollowers(username)
        }
        return networkBoundResource.asFlow()
    }

    override fun fetchUserFollowing(username: String): Flow<Resource<List<Users>>> {
        val networkBoundResource = object : NetworkBoundResource<List<Users>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<Users>> {
                return DataMapper.mapResponsesToUserList(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.fetchUserFollowing(username)
        }
        return networkBoundResource.asFlow()
    }


    override fun fetchAllUserFavorite(): Flow<List<Users>> =
        localDataSource.getAllUserFavorite()
            .map { userEntities -> DataMapper.mapEntitiesToUsersList(userEntities) }

    override suspend fun addUserFavorite(users: Users) {
        val userEntity = DataMapper.mapUserToEntity(users)
        localDataSource.insertUserFavorite(userEntity)
    }

    override suspend fun deleteUserFavorite(users: Users) {
        val userEntity = DataMapper.mapUserToEntity(users)
        localDataSource.deleteUserFavorite(userEntity)
    }

    override fun getFavoriteIsExists(username: String): Flow<Boolean> {
        return checkFavoriteExists(username)
    }

    private fun checkFavoriteExists(username: String): Flow<Boolean> {
        return localDataSource.getFavoriteIsExists(username)
    }

    override fun getThemeSetting(): Flow<Boolean> = sharedPreferences.getThemeSetting()

    override suspend fun saveThemeSetting(isDarkModeActivity: Boolean) {
        sharedPreferences.saveThemeSetting(isDarkModeActivity)
    }
}