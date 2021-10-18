package com.aadya.whiskyapp.landing.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.landing.model.LoginResponseModel
import com.aadya.whiskyapp.utils.AlertModel


class LoginViewModel(loginRepository: LoginRepository) : ViewModel() {
    private var loginRepository: LoginRepository = loginRepository

   /* private var logindataMutableLiveData: MutableLiveData<LoginResponseModel>? = MutableLiveData()*/




    fun getLoginViewState(): LiveData<LoginResponseModel?>? {
        return loginRepository.getLoginViewState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return loginRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return loginRepository.getAlertViewState()
    }

    fun checkAuthentication(
        mcontext: Context,
        userNameText: String?,
        passwordText: String?,

        ) {
        loginRepository.checkAuthentication(mcontext, userNameText, passwordText)
    }




}