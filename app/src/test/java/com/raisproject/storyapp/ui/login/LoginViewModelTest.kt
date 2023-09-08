package com.raisproject.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.raisproject.storyapp.data.Repository
import com.raisproject.storyapp.data.Result
import com.raisproject.storyapp.data.response.LoginResponse
import com.raisproject.storyapp.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLogin = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `when Login Should Not Null and Return Success`() {
        val expectedUser = MutableLiveData<Result<LoginResponse>>()
        expectedUser.value = Result.Success(dummyLogin)
        `when`(loginViewModel.postLogin(EMAIL, PASSWORD)).thenReturn(expectedUser)

        val actualUser = loginViewModel.postLogin(EMAIL, PASSWORD)

        Mockito.verify(repository).postLogin(EMAIL, PASSWORD)
        Assert.assertNotNull(actualUser)
    }

    companion object {
        private const val EMAIL = "email@gmail.com"
        private const val PASSWORD = "12345678"
    }
}