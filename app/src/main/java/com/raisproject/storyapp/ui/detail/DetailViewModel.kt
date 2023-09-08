package com.raisproject.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.raisproject.storyapp.data.Repository

class DetailViewModel(private val repository: Repository) : ViewModel() {

    fun getDetailStory(id: String, token: String) = repository.getDetailStory(id, token)
}