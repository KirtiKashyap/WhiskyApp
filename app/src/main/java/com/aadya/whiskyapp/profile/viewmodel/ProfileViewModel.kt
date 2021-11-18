package com.aadya.whiskyapp.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class ProfileViewModel(profileRepository: ProfileRepository) : ViewModel() {
    private var profileRepository: ProfileRepository = profileRepository

    fun getProfile(authorization: String?, userId: Int?) {
       profileRepository.getProfile(authorization,userId)
   }

    fun getProfileObserver() : LiveData<ProfileResponseModel?>{
        return  profileRepository.getProfileState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return profileRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return profileRepository.getAlertViewState()
    }

    fun getprofileUnAuthorized() : LiveData<Boolean>{
        return  profileRepository.getprofileUnAuthorized()
    }

    fun getEventNotification(authorization: String?, userId: Int?) {
        profileRepository.getEventNotification(authorization,userId)
    }
    fun getEventNotificationObserver():LiveData<Any?>{
        return profileRepository.getEventNotificationState()
    }


    fun getSpecialOfferNotification(authorization: String?, userId: Int?) {
        profileRepository.getSpecialOfferNotification(authorization,userId)
    }
    fun getSpecialOfferNotificationObserver():LiveData<Any?>{
        return profileRepository.getSpecialOfferNotificationState()
    }

}