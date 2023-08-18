package com.example.dogedex.platform.di.module

import com.example.dogedex.data.repository.DogRepositoryImpl
import com.example.dogedex.data.repository.AuthRepositoryImpl
import com.example.dogedex.domain.usecase.DogUseCase
import com.example.dogedex.domain.usecase.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun dogUseCaseProvider(dogRepositoryImpl : DogRepositoryImpl) =
        DogUseCase(dogRepositoryImpl)

    @Provides
    fun userUseCaseProvider(userRepositoryImpl : AuthRepositoryImpl) =
        AuthUseCase(userRepositoryImpl)
}