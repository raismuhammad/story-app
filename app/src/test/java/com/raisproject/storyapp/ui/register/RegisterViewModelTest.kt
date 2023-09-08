package com.raisproject.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.raisproject.storyapp.data.Repository
import com.raisproject.storyapp.data.Result
import com.raisproject.storyapp.data.response.ErrorResponse
import com.raisproject.storyapp.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummySignUp = DataDummy.generateDummyRegisterResponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when Sign Up Should Not Null and Return Success`() {
        val expectedUser = MutableLiveData<Result<ErrorResponse>>()
        expectedUser.value = Result.Success(dummySignUp)
        Mockito.`when`(registerViewModel.postRegister(NAME, EMAIL, PASSWORD)).thenReturn(expectedUser)

        val actualUser = registerViewModel.postRegister(NAME, EMAIL, PASSWORD)

        Mockito.verify(repository).postRegister(NAME, EMAIL, PASSWORD)
        Assert.assertNotNull(actualUser)
    }

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "email@gmail.com"
        private const val PASSWORD = "password"
    }

}