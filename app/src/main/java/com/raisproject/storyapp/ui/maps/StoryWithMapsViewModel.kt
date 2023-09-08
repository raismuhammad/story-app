package com.raisproject.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raisproject.storyapp.data.Repository
import com.raisproject.storyapp.data.Result
import com.raisproject.storyapp.data.response.StoryResponse

class StoryWithMapsViewModel(private val repository: Repository) : ViewModel() {

    fun getStoriesWithMaps(location: Int, token: String) : LiveData<Result<StoryResponse>> = repository.getStoriesWithLocation(location, token)
}