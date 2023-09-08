package com.raisproject.storyapp.ui.userstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.raisproject.storyapp.data.Repository
import com.raisproject.storyapp.data.response.ListStoryItem

class UserStoryViewModel(private val repository: Repository) : ViewModel() {

    fun story(token: String): LiveData<PagingData<ListStoryItem>> = repository.getStories(token).cachedIn(viewModelScope)

}