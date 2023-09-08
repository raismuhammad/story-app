package com.raisproject.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.raisproject.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = null
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val getName = intent.getStringExtra("name").toString()
        val getImage = intent.getStringExtra("image").toString()
        val getDesc = intent.getStringExtra("desc").toString()

        showDescription(getName, getImage, getDesc)

    }

    private fun showDescription(name: String, image: String, desc: String) {
        binding.tvDescription.text = desc
        binding.tvName.text = name
        Glide.with(this)
            .load(image)
            .into(binding.ivImage)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}