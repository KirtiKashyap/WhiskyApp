package com.aadya.whiskyapp.purchasehistory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class PurchaseHistoryViewModel(purchaseHistoryRepository: PurchaseHistoryRepository) : ViewModel() {
    private var purchaseHistoryRepository: PurchaseHistoryRepository = purchaseHistoryRepository

    fun getPurchaseHistoryList(authorization: String?) {
        purchaseHistoryRepository.getPurchaseHistoryList(authorization)
   }

    fun getPurchaseHistoryListObserver() : MutableLiveData<List<SpecialOfferResponseModel>?> {
        return  purchaseHistoryRepository.getPurchaseHistoryList()
    }

    fun getProgressState(): LiveData<Int?>? {
        return purchaseHistoryRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return purchaseHistoryRepository.getAlertViewState()
    }

    fun getPurchaseHistoryUnAuthorized() : LiveData<Boolean>{
        return  purchaseHistoryRepository.getPurchaseHistoryUnAuthorized()
    }

}