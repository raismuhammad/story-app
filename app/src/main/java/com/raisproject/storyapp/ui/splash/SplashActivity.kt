package com.raisproject.storyapp.ui.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.raisproject.storyapp.R
import com.raisproject.storyapp.data.preferences.User
import com.raisproject.storyapp.data.preferences.UserPreferences
import com.raisproject.storyapp.databinding.ActivitySplashBinding
import com.raisproject.storyapp.ui.login.LoginActivity
import com.raisproject.storyapp.ui.userstory.UserStoryActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferences: UserPreferences
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = UserPreferences.getInstance(this)
        user = preferences.getUser()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(R.color.black)
        }

        Handler().postDelayed({
            if (user.token == "") {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, UserStoryActivity::class.java))
            }
        }, 2000)
    }
}