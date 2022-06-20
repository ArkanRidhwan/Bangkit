package com.inc.vr.corps.storymaps.model.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.inc.vr.corps.storymaps.model.data.StoryPagingSource
import com.inc.vr.corps.storymaps.model.service.ApiService
import com.inc.vr.corps.storymaps.helper.wrapEspressoIdleResource
import com.inc.vr.corps.storymaps.model.ListStoryItem
import com.inc.vr.corps.storymaps.model.StoryResponse
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

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val storyPagingSource: StoryPagingSource
) {


    private val _responseUpload = MutableLiveData<StoryResponse>()
    val responseUpload: LiveData<StoryResponse> = _responseUpload

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getStoriesPaging(): LiveData<PagingData<ListStoryItem>> {
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


    fun uploadStory(token: String, description: String, imgFile: File) {
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImage = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            imgFile.name,
            requestImage
        )
        _loading.value = true
        val client = apiService.uploadStory("Bearer $token", imageMultipart, requestDescription)
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


}