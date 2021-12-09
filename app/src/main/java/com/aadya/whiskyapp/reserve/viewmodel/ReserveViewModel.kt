package com.aadya.whiskyapp.reserve.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.reserve.model.CancelReservationRequest
import com.aadya.whiskyapp.reserve.model.ReserveInfoRequest
import com.aadya.whiskyapp.reserve.model.ReserveInfoResponse
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
    fun getReserveInfoViewState(): MutableLiveData<ReserveInfoResponse?>? {
        return reserveRepository.getReserveInfoViewState()
    }
    fun getCancelReservationViewState(): MutableLiveData<Int?>? {
        return reserveRepository.getCancelReservationViewState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return reserveRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return reserveRepository.getAlertViewState()
    }

    fun getReserveInfo(
        mcontext: Context,
        reserveInfoRequest: ReserveInfoRequest,
        authorization: String?
    ){
        reserveRepository.getReservationInfo(mcontext,reserveInfoRequest,authorization)
    }
    fun getCancelReservation(
        mcontext: Context,
        cancelReservationRequest: CancelReservationRequest,
        authorization: String?
    ){
        reserveRepository.getCancelReservation(mcontext,cancelReservationRequest,authorization)
    }
    fun checkReserveValidation(
        mcontext: Context,
        what_u_want_to_eat: String?,
        date: String?, time: String?, no_of_people: String?, userID: Int?, authorization: String?

    ) {
        reserveRepository.checkReserveValidation(mcontext, what_u_want_to_eat, date,time,no_of_people,userID,authorization)
    }

}