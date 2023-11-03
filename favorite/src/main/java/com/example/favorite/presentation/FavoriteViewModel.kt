package com.example.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.UserUseCase

class FavoriteViewModel(
    private val userUseCase: UserUseCase,
) : ViewModel() {

    fun observeUserFavorite() = userUseCase.gettingAllUserFavorite().asLiveData()
}
