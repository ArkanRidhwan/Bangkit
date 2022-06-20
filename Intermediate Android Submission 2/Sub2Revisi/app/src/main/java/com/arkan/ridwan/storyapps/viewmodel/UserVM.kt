package com.arkan.ridwan.storyapps.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arkan.ridwan.storyapps.api.repo.UserRepo
import com.arkan.ridwan.storyapps.model.LoginResult
import com.arkan.ridwan.storyapps.model.LoginUser
import com.arkan.ridwan.storyapps.model.RegisterUser
import com.arkan.ridwan.storyapps.model.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(
    private val userRepo: UserRepo,
    private val preference: UserPreference
) : ViewModel() {

    val isLoading: LiveData<Boolean> = userRepo.loading
    val userStatus: LiveData<Boolean> = userRepo.userStatus
    val loginResult: LiveData<LoginResult> = userRepo.loginData

    fun userLogin(loginUser: LoginUser, context: Context) {
        userRepo.userLogin(loginUser, context)

    }

    fun userRegister(registerUser: RegisterUser, context: Context) {
        userRepo.userRegister(registerUser, context)
    }

    fun saveUserPreference(loginResult: LoginResult) {
        viewModelScope.launch {
            preference.saveUserData(loginResult)
        }

    }

    fun getUserPreferences(): LiveData<LoginResult> {
        return preference.getUserData().asLiveData()
    }

    fun clearUserPreference() {
        viewModelScope.launch {
            preference.clearUserData()
        }
    }
}