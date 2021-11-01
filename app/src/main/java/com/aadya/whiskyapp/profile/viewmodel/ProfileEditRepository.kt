package com.aadya.whiskyapp.profile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aadya.whiskyapp.profile.model.ProfileEditRequestModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.profile.model.ProfileRequestModel
import com.aadya.whiskyapp.retrofit.APIResponseListener
import com.aadya.whiskyapp.retrofit.RetrofitService
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.Connection
import retrofit2.Response

class ProfileEditRepository(application: Application) {
    private var application : Application = application

    private val profileeditLiveData = MutableLiveData<ProfileResponseModel>()
    private val alertLiveData: MutableLiveData<AlertModel> = MutableLiveData<AlertModel>()
    private val progressLiveData = MutableLiveData<Int>()
    private var profileUnAuthorizedLiveData = MutableLiveData<Boolean>()

    fun getprofileUnAuthorized(): MutableLiveData<Boolean> {
        return profileUnAuthorizedLiveData
    }
    fun getProgressState(): MutableLiveData<Int> {
        return progressLiveData
    }

    fun getAlertViewState(): MutableLiveData<AlertModel>{
        return alertLiveData
    }

    fun getProfileEditState() : MutableLiveData<ProfileResponseModel> {
        return  profileeditLiveData
    }

    fun editProfile(authorization: String, profileRequestModel: ProfileEditRequestModel) {

        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().getProfileEdit(authorization,profileRequestModel,
                object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {
                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog
                        val model: ProfileResponseModel = response.body() as ProfileResponseModel
                        try {
                            if(response.code() == 401)
                                profileUnAuthorizedLiveData.value = true

                            else if(response.code() == 200)
                                profileeditLiveData.value = model

                            else if(response.code() == 500)
                                setAlert(
                                    application.getString(R.string.app_error),
                                    application.getString(R.string.Error_from_Server),
                                    false
                                )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    override fun onFailure() {
                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog
                    }
                })
        } else setAlert(
            application.getString(R.string.no_internet_connection),
            application.getString(R.string.not_connected_to_internet),
            false
        )


    }
    private fun setAlert(title: String, message: String, isSuccess: Boolean) {
        val drawable: Int = if (isSuccess) R.drawable.correct_icon else R.drawable.wrong_icon
        val color: Int = if (isSuccess) R.color.notiSuccessColor else R.color.notiFailColor
        alertLiveData.value = AlertModel(2000, title, message, drawable, color)
    }


}
