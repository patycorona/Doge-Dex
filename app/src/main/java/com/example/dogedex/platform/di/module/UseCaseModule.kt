package com.example.dogedex.platform.di.module

import com.example.dogedex.data.repository.DogRepositoryImpl
import com.example.dogedex.domain.usecase.DogUseCase
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
}