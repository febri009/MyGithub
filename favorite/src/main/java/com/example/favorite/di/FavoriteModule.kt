package com.example.favorite.di

import com.example.core.domain.usecase.UserUseCase
import com.example.favorite.presentation.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel {
        val userUseCase = get<UserUseCase>()
        FavoriteViewModel(userUseCase)
    }
}