package com.example.core.utils

import com.example.core.data.source.local.entity.UserEntity
import com.example.core.data.source.remote.response.UserResponse
import com.example.core.domain.model.Users
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object DataMapper {

    fun mapUserToEntity(input: Users) = UserEntity(
        login = input.login,
        name = input.name,
        avatarUrl = input.avatarUrl,
        followers = input.followers,
        following = input.following,
    )

    fun mapResponsesToUserList(input: List<UserResponse>): Flow<List<Users>> {
        return flow {
            val userList = input.map {
                Users(
                    login = it.login,
                    name = it.name,
                    avatarUrl = it.avatarUrl,
                    followers = it.followers,
                    following = it.following,
                )
            }
            emit(userList)
        }
    }

    fun mapResponsesToUserList(input: UserResponse): Flow<Users> {
        return flow {
            val user = Users(
                login = input.login,
                name = input.name,
                avatarUrl = input.avatarUrl,
                followers = input.followers,
                following = input.following,
            )
            emit(user)
        }
    }

    fun mapEntitiesToUsersList(input: List<UserEntity>): List<Users> =
        input.map {
            Users(
                login = it.login,
                name = it.name,
                avatarUrl = it.avatarUrl,
                followers = it.followers,
                following = it.following,
            )
        }
}