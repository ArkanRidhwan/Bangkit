package com.inc.vr.corps.storymaps.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.inc.vr.corps.storymaps.databinding.ItemLoadingBinding

class LoadingPagingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingPagingAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if(loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }
            binding.apply {
                loadingBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Loading
                tvError.isVisible = loadState is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, retry)
    }
}