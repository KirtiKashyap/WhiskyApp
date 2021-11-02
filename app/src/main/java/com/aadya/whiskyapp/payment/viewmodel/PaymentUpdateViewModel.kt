package com.aadya.whiskyapp.payment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.events.model.RSVPResponseModel
import com.aadya.whiskyapp.payment.model.PaymentResponse
import com.aadya.whiskyapp.payment.model.PaymentUpdate
import com.aadya.whiskyapp.reserve.model.ReserveResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class PaymentUpdateViewModel(paymentRepository: PaymentRepository): ViewModel() {

    private var paymentRepository: PaymentRepository = paymentRepository

    fun getPaymentUpdate(authorization: String,paymentUpdate: PaymentUpdate) {
        paymentRepository.paymentUpdate(authorization,paymentUpdate)
    }


    fun getPaymentObserverState() : MutableLiveData<PaymentResponse?>{
        return  paymentRepository.getPaymentState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return paymentRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return paymentRepository.getAlertViewState()
    }
    fun getPaymentUnAuthorized() : LiveData<Boolean>{
        return  paymentRepository.getPaymentUnAuthorized()
    }
}