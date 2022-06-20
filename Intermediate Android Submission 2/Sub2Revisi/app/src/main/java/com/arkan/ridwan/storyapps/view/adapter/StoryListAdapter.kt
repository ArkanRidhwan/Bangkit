package com.arkan.ridwan.storyapps.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkan.ridwan.storyapps.R
import com.arkan.ridwan.storyapps.databinding.ItemStoryBinding
import com.arkan.ridwan.storyapps.model.ListStoryItem
import com.bumptech.glide.Glide
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
            var navController: NavController?

            Glide.with(itemView)
                .load(dataItem.photoUrl)
                .centerCrop()
                .thumbnail(0.1f)
                .error(R.drawable.ic_placeholder_article)
                .into(binding.ivImage)
            binding.tvName.text = dataItem.name
            binding.tvDate.text = dataItem.createdAt
            binding.tvDescription.text = dataItem.description
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

            override fun areItemsTheSame(
                oldStory: ListStoryItem,
                newStory: ListStoryItem
            ): Boolean {
                return oldStory == newStory
            }

            override fun areContentsTheSame(
                oldStory: ListStoryItem,
                newStory: ListStoryItem
            ): Boolean {
                return oldStory.name == newStory.name &&
                        oldStory.photoUrl == newStory.photoUrl &&
                        oldStory.id == newStory.id &&
                        oldStory.createdAt == newStory.createdAt &&
                        oldStory.description == newStory.description
            }
        }
    }


}
