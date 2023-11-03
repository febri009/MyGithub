package com.example.mygithub.presentation.detail

import androidx.lifecycle.*
import com.example.core.domain.model.Users
import com.example.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(
    private val userUseCase: UserUseCase,
) : ViewModel() {

    fun fetchUserDetail(username: String) = userUseCase.gettingUserDetail(username).asLiveData()

    fun fetchUserFollowers(username: String) = userUseCase.gettingUserFollowers(username).asLiveData()

    fun fetchUserFollowing(username: String) = userUseCase.gettingUserFollowing(username).asLiveData()

    fun addUserFavorite(user: Users) {
        viewModelScope.launch {
            userUseCase.insertUserFavorite(user)
        }
    }

    fun deleteUserFavorite(users: Users) {
        viewModelScope.launch {
            userUseCase.deleteUserFavorite(users)
        }
    }

    fun checkFavoriteIsExists(username: String) =
        userUseCase.getFavoriteIsExists(username).asLiveData()
}
