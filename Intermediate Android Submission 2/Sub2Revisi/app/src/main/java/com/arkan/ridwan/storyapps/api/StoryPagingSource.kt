package com.arkan.ridwan.storyapps.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arkan.ridwan.storyapps.api.service.ApiService
import com.arkan.ridwan.storyapps.helper.wrapEspressoIdleResource
import com.arkan.ridwan.storyapps.model.ListStoryItem
import com.arkan.ridwan.storyapps.model.UserPreference
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class StoryPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INIT_PAGE_INDEX
            val token = userPreference.getUserData().first().token
            wrapEspressoIdleResource {
                if (token.trim().isNotEmpty()) {
                    Timber.d("token : $token")
                    val responseData =
                        apiService.getStories("Bearer $token", position, params.loadSize)
                    Timber.d("responseMessage : ${responseData.message()}")
                    if (responseData.isSuccessful) {
                        Timber.d("responseData : ${responseData.body()}")
                        LoadResult.Page(
                            data = responseData.body()?.listStory ?: emptyList(),
                            prevKey = if (position == 1) null else position - 1,
                            nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else position + 1
                        )
                    } else {
                        Timber.d("responseData : ${responseData.message()}")
                        LoadResult.Error(Exception("Gagal"))
                    }
                } else {
                    Timber.d("token : $token")
                    LoadResult.Error(Exception("Gagal"))
                }
            }
        } catch (e: Exception) {
            Timber.d("error : ${e.message}")
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INIT_PAGE_INDEX = 1
    }
}