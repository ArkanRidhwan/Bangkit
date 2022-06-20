package com.inc.vr.corps.storymaps.view.ui.islogin.story

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.inc.vr.corps.storymaps.BuildConfig
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.FragmentLoginBinding
import com.inc.vr.corps.storymaps.databinding.FragmentStoryBinding
import com.inc.vr.corps.storymaps.model.LoginUser
import com.inc.vr.corps.storymaps.view.adapter.LoadingPagingAdapter
import com.inc.vr.corps.storymaps.view.adapter.StoryListAdapter
import com.inc.vr.corps.storymaps.view.adapter.StoryLoadStateAdapter
import com.inc.vr.corps.storymaps.view.ui.islogin.MainActivity
import com.inc.vr.corps.storymaps.view.ui.nologin.NoLoginActivity
import com.inc.vr.corps.storymaps.viewmodel.StoryViewModel
import com.inc.vr.corps.storymaps.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class StoryFragment :  Fragment(R.layout.fragment_story) {
    private lateinit var binding: FragmentStoryBinding
    private val userViewModel by viewModels<UserViewModel>()
    private val storyViewModel by viewModels<StoryViewModel>()
    private lateinit var storiesAdapter: StoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar, true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_story, container, false)
            //mengganti theme
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Timber BuildConfig
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        setupAdapter()
        setupData()
    }

    private fun setupAdapter() {
        storiesAdapter = StoryListAdapter()
        binding.rvStoryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStoryList.adapter = storiesAdapter.withLoadStateFooter(
            footer = StoryLoadStateAdapter {
                storiesAdapter.retry()
            }
        )
        binding.btnRetry.setOnClickListener {
            storiesAdapter.retry()
        }
        storiesAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvStoryList.isVisible = loadState.source.refresh is LoadState.NotLoading
                tvErrorInfo.isVisible = loadState.source.refresh is LoadState.Error
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    storiesAdapter.itemCount < 1
                ) {
                    rvStoryList.isVisible = false
                    tvEmpty.isVisible = true
                } else {
                    tvEmpty.isVisible = false
                }
            }
        }
    }
    private fun setupData() {
        storyViewModel.allStory.observe(requireActivity()) { pagingData ->
            storiesAdapter.submitData(lifecycle, pagingData)
        }

    }
}