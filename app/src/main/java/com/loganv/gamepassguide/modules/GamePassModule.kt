package com.loganv.gamepassguide.modules

import com.loganv.gamepassguide.apis.GamePassApi
import com.loganv.gamepassguide.apis.GamePassInfoApi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamesModule {

    @Singleton
    @Provides
    fun provideGamePassApi(
        // Potential dependencies of this type
    ): GamePassApi {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(GamePassApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGamePassInfoApi(
        // Potential dependencies of this type
    ): GamePassInfoApi {
        return Retrofit.Builder()
            .baseUrl("https://docs.google.com")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(GamePassInfoApi::class.java)
    }
}
