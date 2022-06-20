package com.arkan.ridwan.storyapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkan.ridwan.storyapps.api.repo.StoryRepo
import com.arkan.ridwan.storyapps.model.ListStoryItem
import com.arkan.ridwan.storyapps.model.ListStoryMaps
import com.arkan.ridwan.storyapps.model.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StoryVM @Inject constructor(private val storyRepo: StoryRepo) :
    ViewModel() {
    val allStory: LiveData<PagingData<ListStoryItem>> =
        storyRepo.getPStories().cachedIn(viewModelScope)

    val isLoading: LiveData<Boolean> = storyRepo.loading
    val dataStoryWithLocation: LiveData<List<ListStoryMaps>> = storyRepo.storyWithLoc
    val responseUpload: LiveData<StoryResponse> = storyRepo.responseUpload


    fun uploadStory(
        token: String,
        description: String,
        imgFile: File,
        lat: Float,
        lon: Float
    ) {
        storyRepo.uploadStory(token, description, imgFile, lat, lon)
    }

    fun getStoryWithLocation(token: String) {
        storyRepo.getStoriesLoc(token)
    }

}