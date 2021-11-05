package com.aadya.whiskyapp.purchasehistory.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.purchasehistory.model.PurchaseHistory
import com.aadya.whiskyapp.retrofit.APIResponseListener
import com.aadya.whiskyapp.retrofit.RetrofitService
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.Connection
import retrofit2.Response

class PurchaseHistoryRepository(application: Application) {
    private var application : Application = application
    private val purchaseHistoryLiveData = MutableLiveData<List<PurchaseHistory>?>()
    private val alertLiveData: MutableLiveData<AlertModel> = MutableLiveData<AlertModel>()
    private val progressLiveData = MutableLiveData<Int>()
    private var purchaseHistoryUnAuthorizedLiveData = MutableLiveData<Boolean>()

    fun getPurchaseHistoryUnAuthorized(): MutableLiveData<Boolean> {
        return purchaseHistoryUnAuthorizedLiveData
    }

    fun getProgressState(): MutableLiveData<Int>? {
        return progressLiveData
    }

    fun getAlertViewState(): MutableLiveData<AlertModel>? {
        return alertLiveData
    }

    fun getPurchaseHistoryList() : MutableLiveData<List<PurchaseHistory>?> {
        return  purchaseHistoryLiveData
    }

    fun getPurchaseHistoryList(authorization: String?) {

        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().getPurchaseHistoryList(authorization,
                object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {

                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog

                        val modelList: List<PurchaseHistory>? = response.body() as ArrayList<PurchaseHistory>?
                        try {


                            if(response.code() == 401)
                                purchaseHistoryUnAuthorizedLiveData.value = true

                            else if(response.code() == 200)
                                purchaseHistoryLiveData.value = modelList


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
