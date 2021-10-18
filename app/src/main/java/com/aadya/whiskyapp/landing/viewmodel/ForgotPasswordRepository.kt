package com.aadya.whiskyapp.landing.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.aadya.gist.login.model.LoginRequestModel
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.retrofit.APIResponseListener
import com.aadya.whiskyapp.retrofit.RetrofitService
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.Connection
import retrofit2.Response

class ForgotPasswordRepository(application: Application) {
    private var application : Application = application
    private var mCommonUtils : CommonUtils = CommonUtils


    private val forgotpasswordliveData: MutableLiveData<AlertModel?> = MutableLiveData<AlertModel?>()
    private val progressLiveData = MutableLiveData<Int>()
    private val alertLiveData: MutableLiveData<AlertModel> = MutableLiveData<AlertModel>()

    fun getForgotPasswordViewState(): MutableLiveData<AlertModel?>? {
        return forgotpasswordliveData
    }

    fun getProgressState(): MutableLiveData<Int>? {
        return progressLiveData
    }

    fun getAlertViewState(): MutableLiveData<AlertModel>? {
        return alertLiveData
    }

    fun checkForgotPasswordAuthentication(mContext : Context,
                            userNameText: String?

    )
    {

        if (userNameText.equals(
                "",
                ignoreCase = true
            )
        ) setAlert(
            mContext.getString(R.string.ForgotPassword_Error),
            mContext.getString(R.string.please_enter_name),
            false
        ) else{
            val loginModel : LoginRequestModel = LoginRequestModel()
            loginModel.username = userNameText


          check_ForgotPassword_Authentication(loginModel,mContext)

        }
    }

    private fun check_ForgotPassword_Authentication(
        loginRequestModel: LoginRequestModel , mContext : Context
    ) {
        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().checkForgotAuthentication(
                loginRequestModel,
                object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {

                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog

                        try {


                            if (response.code() == 200) {

                                    setAlert(
                                        mContext.getString(R.string.forgot_password),
                                        mContext.getString(R.string.ForgotPassword_Success_Message),
                                        true
                                    )

                            } else  {

                                setAlert(
                                    mContext.getString(R.string.ForgotPassword_Error),
                                    mContext.getString(R.string.sorry_could_not_process),
                                    false
                                )
                            }


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


                    }

                    override fun onFailure() {
                        mCommonUtils.LogMessage("ForgotResult failure")
                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog
                    }
                })
        } else setAlert(
            mContext.getString(R.string.no_internet_connection),
            mContext.getString(R.string.not_connected_to_internet),
            false
        )
    }





    private fun setAlert(title: String, message: String, isSuccess: Boolean) {
        val drawable: Int = if (isSuccess) R.drawable.correct_icon else R.drawable.wrong_icon
        val color: Int = if (isSuccess) R.color.notiSuccessColor else R.color.notiFailColor
        alertLiveData.value = AlertModel(2000, title, message, drawable, color)
    }


}
