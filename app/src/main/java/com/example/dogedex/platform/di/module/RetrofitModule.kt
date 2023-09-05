package com.example.dogedex.platform.di.module

import com.example.dogedex.data.model.response.ApiServiceInterceptor
import com.example.dogedex.data.network.Constant
import com.example.dogedex.data.network.CoreHomeAPI
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {
    var httpClient: OkHttpClient

    init {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient = OkHttpClient.Builder()
            .addInterceptor(ApiServiceInterceptor)
            .addNetworkInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder()
                        .build()
                chain.proceed(request)
            }.build()
    }

    @Provides
    fun provideRetrofitCoreHomeAPIit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constant.URL_BASE)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()

    @Provides
    fun provideCoreHomeApi(retrofit: Retrofit): CoreHomeAPI {
        return retrofit.create(CoreHomeAPI::class.java)
    }
}