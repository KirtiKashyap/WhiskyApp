package com.aadya.whiskyapp.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.profile.model.ProfileEditRequestModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class ProfileEditViewModel(profileeditRepository: ProfileEditRepository) : ViewModel() {
    private var profileeditRepository: ProfileEditRepository = profileeditRepository

    fun editProfile(authorization: String, profileEditRequestModel: ProfileEditRequestModel) {
        profileeditRepository.editProfile(authorization,profileEditRequestModel)
   }

    fun getEditProfileObserver() : LiveData<ProfileResponseModel>{
        return  profileeditRepository.getProfileEditState()
    }

    fun getProgressState(): LiveData<Int> {
        return profileeditRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel> {
        return profileeditRepository.getAlertViewState()
    }
    fun getprofileUnAuthorized() : LiveData<Boolean>{
        return  profileeditRepository.getprofileUnAuthorized()
    }

}