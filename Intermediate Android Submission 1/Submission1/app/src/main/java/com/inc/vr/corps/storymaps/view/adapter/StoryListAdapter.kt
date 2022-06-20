package com.inc.vr.corps.storymaps.view.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.ItemStoryBinding
import com.inc.vr.corps.storymaps.model.ListStoryItem
import timber.log.Timber

class StoryListAdapter : PagingDataAdapter<ListStoryItem, StoryListAdapter.StoryViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val dataItem = getItem(position)
        if (dataItem != null) {
            holder.bind(dataItem)
        }
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: ListStoryItem) {
            val context = itemView.context
            var navController: NavController? = null
            binding.storylist = dataItem
            //val imgUri = currentGame.backgroundImage.toUri().buildUpon().scheme("https").build()
            Glide.with(itemView)
                .load(dataItem.photoUrl)
                .centerCrop()
                .thumbnail(0.1f)
                .error(R.drawable.ic_placeholder_article)
                .into(binding.ivImage)

            itemView.setOnClickListener {
                val bundle = bundleOf(
                    "name" to dataItem.name,
                    "id" to dataItem.id,
                    "image" to dataItem.photoUrl,
                    "description" to dataItem.description,
                    "date" to dataItem.createdAt
                )
                navController = Navigation.findNavController(itemView)
                navController!!.navigate(R.id.action_storyFragment_to_detailFragment, bundle)
                Timber.d("navigate to detail ${bundle.get("image")}")
            }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {

            override fun areItemsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory == newStory
            }

            override fun areContentsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory.name == newStory.name &&
                        oldStory.photoUrl == newStory.photoUrl &&
                        oldStory.id == newStory.id &&
                        oldStory.createdAt == newStory.createdAt &&
                        oldStory.description == newStory.description
            }
        }
    }


}
