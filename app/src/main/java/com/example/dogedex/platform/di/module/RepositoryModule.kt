package com.example.dogedex.platform.di.module

import com.example.dogedex.data.network.CoreHomeAPI
import com.example.dogedex.data.repository.AuthRepositoryImpl
import com.example.dogedex.data.repository.ClassiFierRepositoryImpl
import com.example.dogedex.data.repository.DogRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun dogRepositoryProvider(coreHomeAPI: CoreHomeAPI): DogRepositoryImpl =
        DogRepositoryImpl(coreHomeAPI)

    @Provides
    fun userRepositoryProvider(coreHomeAPI: CoreHomeAPI): AuthRepositoryImpl =
        AuthRepositoryImpl(coreHomeAPI)

    @Provides
    fun classiFierRepositoryProvider(): ClassiFierRepositoryImpl =
        ClassiFierRepositoryImpl()
}