package com.raisproject.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisproject.storyapp.data.response.ListStoryItem
import com.raisproject.storyapp.databinding.StoryItemBinding
import com.raisproject.storyapp.ui.detail.DetailActivity
import com.raisproject.storyapp.utils.DateFormatter
import java.util.TimeZone

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {


    class StoryViewHolder(val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(ivImage)
                tvTitle.text = story.name?.capitalize()
                tvSubtitle.text = story.description?.capitalize()
                tvDate.text = DateFormatter.formatDate(story.createdAt.toString(), TimeZone.getDefault().id)
                cardview.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("name", story.name)
                    intent.putExtra("image", story.photoUrl)
                    intent.putExtra("desc", story.description)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}