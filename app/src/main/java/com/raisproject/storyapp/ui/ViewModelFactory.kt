package com.raisproject.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raisproject.storyapp.data.Repository
import com.raisproject.storyapp.di.Injection
import com.raisproject.storyapp.ui.addstory.AddStoryViewModel
import com.raisproject.storyapp.ui.detail.DetailViewModel
import com.raisproject.storyapp.ui.login.LoginViewModel
import com.raisproject.storyapp.ui.maps.StoryWithMapsViewModel
import com.raisproject.storyapp.ui.register.RegisterViewModel
import com.raisproject.storyapp.ui.userstory.UserStoryViewModel

class ViewModelFactory private constructor(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UserStoryViewModel::class.java) -> {
                return UserStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                return AddStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryWithMapsViewModel::class.java) -> {
                return StoryWithMapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}