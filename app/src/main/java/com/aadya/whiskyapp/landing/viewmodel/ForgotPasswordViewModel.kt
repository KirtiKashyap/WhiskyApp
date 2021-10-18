package com.aadya.whiskyapp.landing.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.utils.AlertModel


class ForgotPasswordViewModel(forgotPasswordRepository: ForgotPasswordRepository) : ViewModel() {
    private var forgotPasswordRepository: ForgotPasswordRepository = forgotPasswordRepository





    fun getForgotPasswordViewState(): LiveData<AlertModel?>? {
        return forgotPasswordRepository.getForgotPasswordViewState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return forgotPasswordRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return forgotPasswordRepository.getAlertViewState()
    }

    fun checkAuthentication(
        mcontext: Context,
        userNameText: String?


        ) {
        forgotPasswordRepository.checkForgotPasswordAuthentication(mcontext, userNameText)
    }




}