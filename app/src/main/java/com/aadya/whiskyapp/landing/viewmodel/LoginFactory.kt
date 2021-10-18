package com.aadya.whiskyapp.landing.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.utils.SessionManager

class LoginFactory(private val application: Application , private val mSessionManager : SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) LoginViewModel(
            LoginRepository(
                application ,mSessionManager
            )
        ) as T else throw IllegalArgumentException("Unknown ViewModel class")
    }
}