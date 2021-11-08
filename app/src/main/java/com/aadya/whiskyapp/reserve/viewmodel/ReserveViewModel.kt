package com.aadya.whiskyapp.reserve.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.reserve.model.ReserveResponseModel
import com.aadya.whiskyapp.utils.AlertModel


class ReserveViewModel(reserveRepository: ReserveRepository) : ViewModel() {
    private var reserveRepository: ReserveRepository = reserveRepository

    fun getreserveUnAuthorized() : LiveData<Boolean>{
        return  reserveRepository.getreserveUnAuthorized()
    }



    fun getReserveViewState(): MutableLiveData<ReserveResponseModel?>? {
        return reserveRepository.getReserveViewState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return reserveRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return reserveRepository.getAlertViewState()
    }

    fun checkReserveValidation(
        mcontext: Context,
        what_u_want_to_eat: String?,
        date: String?, time: String?, no_of_people: String?, userID: Int?, authorization: String?

    ) {
        reserveRepository.checkReserveValidation(mcontext, what_u_want_to_eat, date,time,no_of_people,userID,authorization)
    }

}