package com.raisproject.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.raisproject.storyapp.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    fun postRegister(name: String, email: String, password: String) = repository.postRegister(name, email, password)
}