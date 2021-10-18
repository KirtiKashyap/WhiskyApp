package com.aadya.whiskyapp.specialoffers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class SpecialOfferViewModel(specialOfferRepository: SpecialOfferRepository) : ViewModel() {
    private var specialOfferRepository: SpecialOfferRepository = specialOfferRepository


    fun getSpecialOffer(authorization: String?) {
       specialOfferRepository.getSpecialOffer(authorization)
   }

    fun getSpecialOfferObserver() : MutableLiveData<List<SpecialOfferResponseModel>?> {
        return  specialOfferRepository.getSpecialOfferState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return specialOfferRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return specialOfferRepository.getAlertViewState()
    }
    fun getprofileUnAuthorized() : LiveData<Boolean>{
        return  specialOfferRepository.getprofileUnAuthorized()
    }

}