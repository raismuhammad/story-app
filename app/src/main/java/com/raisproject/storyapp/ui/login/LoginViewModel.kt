package com.raisproject.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.raisproject.storyapp.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun postLogin(email: String, password: String) = repository.postLogin(email, password)

}