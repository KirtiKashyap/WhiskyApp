package com.aadya.whiskyapp.landing.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForgotPasswordFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) ForgotPasswordViewModel(
            ForgotPasswordRepository(application)
        ) as T else throw IllegalArgumentException("Unknown ViewModel class")
    }
}