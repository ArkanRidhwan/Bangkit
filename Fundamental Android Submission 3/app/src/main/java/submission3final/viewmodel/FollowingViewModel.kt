package submission3final.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import submission3final.network.ApiConfig
import submission3final.model.response.FollowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    val followingList = MutableLiveData<List<FollowResponse>>()

    fun setFowllowing(username: String, mContext: Context) {
        val client = ApiConfig.getApiService(mContext).getFollowing(username)
        client.enqueue(object : Callback<List<FollowResponse>> {
            override fun onResponse(
                call: Call<List<FollowResponse>>,
                response: Response<List<FollowResponse>>
            ) {
                val userResp = response.body()
                if (userResp!!.isNotEmpty()) {
                    followingList.postValue(userResp!!)
                }
            }

            override fun onFailure(call: Call<List<FollowResponse>>, t: Throwable) {
                Log.e("User", "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(): MutableLiveData<List<FollowResponse>> {
        return followingList
    }

}