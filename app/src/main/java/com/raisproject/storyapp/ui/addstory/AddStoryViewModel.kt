package com.raisproject.storyapp.ui.addstory

import androidx.lifecycle.ViewModel
import com.raisproject.storyapp.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: Repository) : ViewModel() {

    fun uploadStory(file: MultipartBody.Part, description: RequestBody, lat: Double, lon: Double, token: String) = repository.uploadStory(file, description, lat, lon, token)
}