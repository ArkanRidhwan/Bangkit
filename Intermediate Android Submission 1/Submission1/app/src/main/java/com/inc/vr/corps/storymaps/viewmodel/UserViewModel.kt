package com.inc.vr.corps.storymaps.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.inc.vr.corps.storymaps.model.LoginResult
import com.inc.vr.corps.storymaps.model.LoginUser
import com.inc.vr.corps.storymaps.model.RegisterUser
import com.inc.vr.corps.storymaps.model.UserPreference
import com.inc.vr.corps.storymaps.model.repo.UserRepository
import com.inc.vr.corps.storymaps.view.ui.islogin.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: UserPreference
) : ViewModel() {

    val isLoading: LiveData<Boolean> = userRepository.loading
    val userStatus: LiveData<Boolean> = userRepository.userStatus
    val loginResult: LiveData<LoginResult> = userRepository.loginData

    fun userLogin(loginUser: LoginUser,context: Context) {
        userRepository.userLogin(loginUser,context)

    }

    fun userRegister(registerUser: RegisterUser, context: Context) {
        userRepository.userRegister(registerUser, context)
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