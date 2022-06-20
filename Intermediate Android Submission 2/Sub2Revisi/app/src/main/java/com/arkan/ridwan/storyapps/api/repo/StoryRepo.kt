package com.arkan.ridwan.storyapps.api.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.arkan.ridwan.storyapps.api.StoryPagingSource
import com.arkan.ridwan.storyapps.api.service.ApiService
import com.arkan.ridwan.storyapps.helper.wrapEspressoIdleResource
import com.arkan.ridwan.storyapps.model.ListStoryItem
import com.arkan.ridwan.storyapps.model.ListStoryMaps
import com.arkan.ridwan.storyapps.model.StoryResponse
import com.arkan.ridwan.storyapps.model.StoryResponseMaps
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class StoryRepo @Inject constructor(
    private val apiService: ApiService,
    private val storyPagingSource: StoryPagingSource
) {
    private val _storyWithLoc = MutableLiveData<List<ListStoryMaps>>()
    val storyWithLoc: LiveData<List<ListStoryMaps>> = _storyWithLoc

    private val _responseUpload = MutableLiveData<StoryResponse>()
    val responseUpload: LiveData<StoryResponse> = _responseUpload
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    fun getPStories(): LiveData<PagingData<ListStoryItem>> {
        wrapEspressoIdleResource {
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    storyPagingSource
                }
            ).liveData
        }
    }


    fun uploadStory(token: String, description: String, imgFile: File, lat: Float, lng: Float) {
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestLat = lat.toString().toRequestBody("text/plain".toMediaType())
        val requestLng = lng.toString().toRequestBody("text/plain".toMediaType())
        val requestImage = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            imgFile.name,
            requestImage
        )
        _loading.value = true
        val client = apiService.uploadStory(
            "Bearer $token", imageMultipart,
            requestDescription, requestLat, requestLng
        )
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _responseUpload.value = response.body()
                    _loading.value = false
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Timber.e(t)
                _loading.value = false
            }

        })
    }

    fun getStoriesLoc(token: String) {
        val client = apiService.getStoriesLoc("Bearer $token")
        client.enqueue(object : Callback<StoryResponseMaps> {
            override fun onResponse(
                call: Call<StoryResponseMaps>,
                response: Response<StoryResponseMaps>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _storyWithLoc.value = responseBody.listStory
                }
            }

            override fun onFailure(call: Call<StoryResponseMaps>, t: Throwable) {
                Timber.e(t)
            }
        })
    }
}