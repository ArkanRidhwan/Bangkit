package com.arkan.ridwan.storyapps.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkan.ridwan.storyapps.BuildConfig
import com.arkan.ridwan.storyapps.R
import com.arkan.ridwan.storyapps.databinding.FragmentStoryBinding
import com.arkan.ridwan.storyapps.view.adapter.StoryListAdapter
import com.arkan.ridwan.storyapps.view.adapter.StoryLoadStateAdapter
import com.arkan.ridwan.storyapps.viewmodel.StoryVM
import com.arkan.ridwan.storyapps.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class StoryFragment : Fragment(R.layout.fragment_story) {
    private lateinit var binding: FragmentStoryBinding
    private val userViewModel by viewModels<UserVM>()
    private val storyViewModel by viewModels<StoryVM>()
    private lateinit var storiesAdapter: StoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(
            androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar,
            true
        );
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_story, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        getUserName()
        setupAdapter()
        setupData()
        setupView()
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

    fun getUserName() {
        userViewModel.getUserPreferences().observe(requireActivity()) {
            binding.tvNama.text = "Hi " + it.name
        }
    }

    fun setupView() {

        binding.btnTambah.setOnClickListener {
            var navController: NavController? = Navigation.findNavController(it)
            val action = StoryFragmentDirections.actionStoryFragmentToAddFragment()
            navController!!.navigate(action)
        }
    }
}