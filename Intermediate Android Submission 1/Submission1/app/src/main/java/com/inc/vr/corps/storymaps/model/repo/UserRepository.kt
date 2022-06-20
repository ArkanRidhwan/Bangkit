package com.inc.vr.corps.storymaps.model.repo

import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.developer.kalert.KAlertDialog
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.model.*
import com.inc.vr.corps.storymaps.model.service.ApiService
import com.inc.vr.corps.storymaps.view.ui.nologin.signup.SignUpFragmentDirections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
) {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _userStatus = MutableLiveData<Boolean>()
    val userStatus: LiveData<Boolean> = _userStatus

    private val _loginData = MutableLiveData<LoginResult>()
    val loginData: LiveData<LoginResult> = _loginData

    fun userLogin(loginUser: LoginUser, context: Context) {
        _loading.value = true
        val client = apiService.loginUser(loginUser)
        val pDialog = KAlertDialog(context, KAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
        pDialog.show()
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _loading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _userStatus.value = true
                    Timber.d("Response: ${responseBody.toString()}")
                    _loginData.value = responseBody.loginResult
                    pDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE)
                    pDialog.titleText = "Success"
                    pDialog.dismiss()
                } else {
                    _userStatus.value = false
                    pDialog.changeAlertType(KAlertDialog.ERROR_TYPE)
                    pDialog.titleText = "Error"
                    pDialog.setConfirmClickListener {
                        pDialog.dismissWithAnimation()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loading.value = false
                pDialog.changeAlertType(KAlertDialog.ERROR_TYPE)
                pDialog.titleText = "Error"
                pDialog.setConfirmClickListener {
                    pDialog.dismissWithAnimation()
                }
                Timber.d("Error: ${t.message}")
            }
        })
    }

    fun userRegister(registerUser: RegisterUser, context: Context) {
        _loading.value = true
        val client = apiService.registerUser(registerUser)
        val pDialog = KAlertDialog(context, KAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
        pDialog.show()
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _loading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _userStatus.value = true
                    pDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE)
                    pDialog.titleText = "Success, Silahkan Login."
                    pDialog.setConfirmClickListener {
                        pDialog.dismissWithAnimation()
                    }
                    Timber.d("Response: ${responseBody.toString()}")
                } else {
                    _userStatus.value = false
                    pDialog.changeAlertType(KAlertDialog.ERROR_TYPE)
                    pDialog.titleText = "Error"
                    pDialog.setConfirmClickListener {
                        pDialog.dismissWithAnimation()
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _loading.value = false
                pDialog.changeAlertType(KAlertDialog.ERROR_TYPE)
                pDialog.titleText = "Error"
                pDialog.setConfirmClickListener {
                    pDialog.dismissWithAnimation()
                }
                Timber.d("Error: ${t.message}")
            }

        })
    }

}