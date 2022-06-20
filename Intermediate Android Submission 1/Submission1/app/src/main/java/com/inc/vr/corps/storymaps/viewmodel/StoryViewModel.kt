package com.inc.vr.corps.storymaps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.inc.vr.corps.storymaps.model.ListStoryItem
import com.inc.vr.corps.storymaps.model.StoryResponse
import com.inc.vr.corps.storymaps.model.repo.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    val allStory: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStoriesPaging().cachedIn(viewModelScope)

    val isLoading: LiveData<Boolean> = storyRepository.loading

    val responseUpload : LiveData<StoryResponse> = storyRepository.responseUpload


    fun uploadStory(
        token: String,
        description: String,
        imgFile: File
    ) {
        storyRepository.uploadStory(token, description, imgFile)
    }

}