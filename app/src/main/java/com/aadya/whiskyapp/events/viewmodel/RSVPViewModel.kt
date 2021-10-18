package com.aadya.whiskyapp.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.events.model.RSVPRequestModel
import com.aadya.whiskyapp.events.model.RSVPResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class RSVPViewModel(rsvpRepository: RSVPRepository) : ViewModel() {
    private var rsvpRepository: RSVPRepository = rsvpRepository

    fun getRSVP(authorization: String?, rsvpRequestModel: RSVPRequestModel) {
        rsvpRepository.getRSVP(authorization,rsvpRequestModel)
   }

    fun getRSVPObserver() : LiveData<RSVPResponseModel?>{
        return  rsvpRepository.getRSVPState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return rsvpRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return rsvpRepository.getAlertViewState()
    }

}