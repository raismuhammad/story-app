package com.raisproject.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.raisproject.storyapp.data.remote.ApiService
import com.raisproject.storyapp.data.response.DetailResponse
import com.raisproject.storyapp.data.response.LoginResponse
import com.raisproject.storyapp.data.response.ErrorResponse
import com.raisproject.storyapp.data.response.ListStoryItem
import com.raisproject.storyapp.data.response.StoryResponse
import com.raisproject.storyapp.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    val apiService: ApiService,
    ) {

    fun postLogin(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(email, password)
            Log.d("TAG", "postLogin-success: ${response.loginResult}")
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("TAG", "postLogin-error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postRegister(name: String, email: String, password: String) : LiveData<Result<ErrorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postRegister(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(token: String) : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    fun getStoriesWithLocation(location: Int, token: String) : LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(location, token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }

    fun getDetailStory(id: String, token: String) : LiveData<Result<DetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(id, token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(file: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?, token: String) : LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(file, description, lat, lon, token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ) : Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }

}