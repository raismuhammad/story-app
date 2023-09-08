package com.raisproject.storyapp.ui.userstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raisproject.storyapp.R
import com.raisproject.storyapp.adapter.LoadingStateAdapter
import com.raisproject.storyapp.adapter.StoryAdapter
import com.raisproject.storyapp.data.preferences.User
import com.raisproject.storyapp.data.preferences.UserPreferences
import com.raisproject.storyapp.databinding.ActivityUserStoryBinding
import com.raisproject.storyapp.ui.ViewModelFactory
import com.raisproject.storyapp.ui.addstory.AddStoryActivity
import com.raisproject.storyapp.ui.login.LoginActivity
import com.raisproject.storyapp.ui.maps.StoryWithMapsActivity

class UserStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserStoryBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var userPreferences: UserPreferences
    private lateinit var factory: ViewModelFactory
    val viewModel: UserStoryViewModel by viewModels() { factory }
    lateinit var user: User
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter()
        userPreferences = UserPreferences(this)
        factory = ViewModelFactory.getInstance(this)
        user = User()
        token = userPreferences.getUser().token.toString()


        showRecyclerView()
        navigation()
        showViewModel()
        toolbar()
    }

    private fun toolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.logout -> {
                    logout()
                    true
                }
                R.id.maps -> {
                    startActivity(Intent(this, StoryWithMapsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showViewModel() {
        val auth = "Bearer $token"
        Log.d("TAG", "auth: ${auth}")
        viewModel.story(auth).observe(this, {
            Log.d("TAG", "Story-List: ${it}")
            storyAdapter.submitData(lifecycle, it)
        })
    }

    private fun logout() {
        userPreferences.deleteUser()
        startActivity(Intent(this@UserStoryActivity, LoginActivity::class.java))
        finish()
    }

    private fun navigation() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@UserStoryActivity, AddStoryActivity::class.java))
        }
    }

    private fun showRecyclerView() {
        binding.rvStories.setHasFixedSize(true)
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}