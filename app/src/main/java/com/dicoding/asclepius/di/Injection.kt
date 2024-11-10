package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.CancerRepository
import com.dicoding.asclepius.data.local.room.CancerDatabase
import com.dicoding.asclepius.data.remote.NewsRepository
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideNewsRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }

    fun provideCancerRepository(context: Context): CancerRepository {
        val database = CancerDatabase.getInstance(context)
        val dao = database.cancerDao()

        return CancerRepository.getInstance(dao)
    }
}