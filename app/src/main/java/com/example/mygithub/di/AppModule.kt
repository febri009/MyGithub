package com.example.mygithub.di

import com.example.core.domain.usecase.UserUseCase
import com.example.core.domain.usecase.UsersInteractor
import com.example.mygithub.presentation.detail.DetailViewModel
import com.example.mygithub.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Module for Use Cases
val useCaseModule = module {
    factory<UserUseCase> { UsersInteractor(get()) }
}

// Module for View Models
val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}