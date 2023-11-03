package com.example.mygithub.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val userUseCase: UserUseCase,
) : ViewModel() {

    fun fetchUsersByUsername(username: String) = userUseCase.gettingUsersByUsername(username).asLiveData()

    fun fetchAppThemeSetting() = userUseCase.gettingThemeSetting().asLiveData()

    fun saveAppThemeSetting(isDarkModeActivity: Boolean) {
        viewModelScope.launch {
            userUseCase.saveThemeSetup(isDarkModeActivity)
        }
    }
}