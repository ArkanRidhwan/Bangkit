package submission3final.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import submission3final.network.ApiConfig
import submission3final.model.response.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
    val _userDetail = MutableLiveData<UserDetailResponse>()

    fun setUserDetail(user: String, mContext: Context) {
        val client = ApiConfig.getApiService(mContext).getUser(user)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val userResp = response.body()
                    if (userResp != null) {
                        _userDetail.postValue(userResp!!)
                    }

                } else {
                    Log.e("User", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e("User", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(): MutableLiveData<UserDetailResponse> {
        return _userDetail
    }
}