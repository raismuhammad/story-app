package com.raisproject.storyapp.ui.register

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
import com.raisproject.storyapp.data.Result
import com.raisproject.storyapp.data.response.ErrorResponse
import com.raisproject.storyapp.databinding.ActivityRegisterBinding
import com.raisproject.storyapp.ui.ViewModelFactory
import com.raisproject.storyapp.ui.login.LoginActivity
import com.raisproject.storyapp.utils.showToast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var factory: ViewModelFactory
    val viewModel: RegisterViewModel by viewModels { factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        emailEditText = findViewById(R.id.emailEditText)

        setupView()
        playAnimation()
        signUp()

    }

    private fun signUp() {
        binding.signupButton.setOnClickListener {
            viewModel.postRegister(
                binding.nameEditText.text.toString(),
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            ).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> onSuccess(result.data)
                        is Result.Loading -> onLoading()
                        is Result.Error -> onFailed(result.message.toString())
                    }
                }
            }
        }
    }


    private fun onSuccess(data: ErrorResponse) {
        binding.progressBar.visibility = View.GONE
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onFailed(message: String) {
        binding.progressBar.visibility = View.GONE
        showToast(this, message)
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTv = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTv = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEt = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEt = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val passwordTv = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(10)
        val passwodEt = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(titleTv, nameTv, nameEt, emailTv, emailEt, passwordTv, passwodEt, signup)
            startDelay = 100
            start()
        }
    }
}