package com.raisproject.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.raisproject.storyapp.R
import com.raisproject.storyapp.custom.EmailEditText
import com.raisproject.storyapp.databinding.ActivityLoginBinding
import com.raisproject.storyapp.ui.register.RegisterActivity
import com.raisproject.storyapp.custom.PasswordEditText
import com.raisproject.storyapp.data.Result
import com.raisproject.storyapp.data.preferences.User
import com.raisproject.storyapp.data.preferences.UserPreferences
import com.raisproject.storyapp.ui.ViewModelFactory
import com.raisproject.storyapp.ui.userstory.UserStoryActivity
import com.raisproject.storyapp.utils.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var userPreferences: UserPreferences
    private lateinit var factory: ViewModelFactory
    val viewModel: LoginViewModel by viewModels{ factory }
    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)
        factory = ViewModelFactory.getInstance(this)
        user = User()

        passwordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)


        setupView()
        playAnimation()
        navigation()

    }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }


    private fun onFailed(message: String?) {
        binding.progressBar.visibility = View.GONE
        showToast(this@LoginActivity, message.toString())
    }

    private fun saveUser(token: String) {
        user.token = token
        userPreferences.setUser(user)
    }

    private fun navigation() {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            viewModel.postLogin(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            ).observe(this) {result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            val data = result.data
                            binding.progressBar.visibility = View.GONE
                            saveUser(data.loginResult.token)
                            val intent = Intent(this@LoginActivity, UserStoryActivity::class.java)
                            startActivity(intent)
                        }
                        is Result.Loading -> onLoading()
                        is Result.Error -> onFailed(result.message)
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTextView = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val messageTextView = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEt = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val passwordTv = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEt = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)
        val loginButton = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val textRegister = ObjectAnimator.ofFloat(binding.textRegister, View.ALPHA, 1f).setDuration(100)
        val registerButton = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleTextView,
                messageTextView,
                emailTv,
                emailEt,
                passwordTv,
                passwordEt,
                loginButton,
                textRegister,
                registerButton)
            startDelay = 100
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}