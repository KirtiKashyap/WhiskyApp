package com.aadya.whiskyapp.payment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.payment.model.PaymentByEmail
import com.aadya.whiskyapp.payment.model.PaymentByEmailResponse
import com.aadya.whiskyapp.payment.model.PaymentResponse
import com.aadya.whiskyapp.payment.model.PaymentUpdate
import com.aadya.whiskyapp.utils.AlertModel

class PaymentUpdateViewModel(paymentRepository: PaymentRepository): ViewModel() {

    private var paymentRepository: PaymentRepository = paymentRepository

    fun paymentByEmail(authorization: String, paymentByEmail: PaymentByEmail) {
        paymentRepository.paymentByEmail(authorization,paymentByEmail)
    }


    fun getPaymentUpdate(authorization: String,paymentUpdate: PaymentUpdate) {
        paymentRepository.paymentUpdate(authorization,paymentUpdate)
    }

    fun getPaymentUpdateObserver() : LiveData<PaymentResponse>{
        return  paymentRepository.getPaymentUpdateState()
    }


    fun getPaymentByEmailObserver() : LiveData<PaymentByEmailResponse?>{
        return  paymentRepository.getPaymentByEmailState()
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