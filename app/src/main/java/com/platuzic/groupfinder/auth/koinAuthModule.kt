package com.platuzic.groupfinder.auth

import com.platuzic.groupfinder.auth.model.AuthRepository
import com.platuzic.groupfinder.auth.viewmodel.LoginViewModel
import com.platuzic.groupfinder.auth.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinAuthModule = module{
    single { AuthRepository(userDao = get()) }
    viewModel { LoginViewModel(authRepository = get()) }
    viewModel { RegisterViewModel(authRepository = get()) }
}