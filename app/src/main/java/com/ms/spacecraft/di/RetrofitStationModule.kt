package com.ms.spacecraft.di

import com.google.gson.GsonBuilder
import com.ms.spacecraft.remote.StationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitStationModule {

    private const val MAIN_URL = "https://run.mocky.io"

    @Singleton
    @Provides
    fun provideRetrofitSpecial(): Retrofit =
        Retrofit.Builder()
            .baseUrl(MAIN_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): StationApi = retrofit.create(StationApi::class.java)

}