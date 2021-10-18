package com.aadya.whiskyapp.specialoffers.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.retrofit.APIResponseListener
import com.aadya.whiskyapp.retrofit.RetrofitService
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.Connection
import retrofit2.Response

class SpecialOfferRepository(application: Application) {
    private var application : Application = application

    private val specialOfferLiveData = MutableLiveData<List<SpecialOfferResponseModel>?>()
    private val alertLiveData: MutableLiveData<AlertModel> = MutableLiveData<AlertModel>()
    private val progressLiveData = MutableLiveData<Int>()
    private var profileUnAuthorizedLiveData = MutableLiveData<Boolean>()

    fun getprofileUnAuthorized(): MutableLiveData<Boolean> {
        return profileUnAuthorizedLiveData
    }

    fun getProgressState(): MutableLiveData<Int>? {
        return progressLiveData
    }

    fun getAlertViewState(): MutableLiveData<AlertModel>? {
        return alertLiveData
    }

    fun getSpecialOfferState() : MutableLiveData<List<SpecialOfferResponseModel>?> {
        return  specialOfferLiveData
    }



    fun getSpecialOffer(authorization: String?) {
        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().getSpecialOffer(authorization,object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {

                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog

                        val modelList: List<SpecialOfferResponseModel>? = response.body() as ArrayList<SpecialOfferResponseModel>?
                        try {


                            if(response.code() == 401)
                                profileUnAuthorizedLiveData.value = true

                            else if(response.code() == 200)
                                specialOfferLiveData.value = modelList

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
