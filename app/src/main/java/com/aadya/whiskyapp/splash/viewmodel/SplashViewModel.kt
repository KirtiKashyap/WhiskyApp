package com.aadya.whiskyapp.splash.viewmodel

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    val splashViewState = MutableLiveData<Boolean>()
    fun checkSplashActivityState() {
        Handler().postDelayed({ splashViewState.value = true }, 3500)
    }
}