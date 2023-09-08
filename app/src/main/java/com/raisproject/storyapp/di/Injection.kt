package com.raisproject.storyapp.di

import android.content.Context
import com.raisproject.storyapp.data.Repository
import com.raisproject.storyapp.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}