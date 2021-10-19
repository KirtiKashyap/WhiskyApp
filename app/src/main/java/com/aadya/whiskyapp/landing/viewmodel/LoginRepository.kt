package com.aadya.whiskyapp.landing.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.aadya.whiskyapp.landing.model.LoginResponseModel
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.landing.model.LoginRequestModel
import com.aadya.whiskyapp.retrofit.APIResponseListener
import com.aadya.whiskyapp.retrofit.RetrofitService
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.Connection
import com.aadya.whiskyapp.utils.SessionManager
import retrofit2.Response

class LoginRepository(application: Application , mSessionManager: SessionManager) {
    private var application : Application = application
    private var mCommonUtils : CommonUtils = CommonUtils
    private var mSessionManager : SessionManager  = mSessionManager


    private val loginliveData: MutableLiveData<LoginResponseModel?> = MutableLiveData<LoginResponseModel?>()
    private val progressLiveData = MutableLiveData<Int>()
    private val alertLiveData: MutableLiveData<AlertModel> = MutableLiveData<AlertModel>()

    fun getLoginViewState(): MutableLiveData<LoginResponseModel?>? {
        return loginliveData
    }

    fun getProgressState(): MutableLiveData<Int>? {
        return progressLiveData
    }

    fun getAlertViewState(): MutableLiveData<AlertModel>? {
        return alertLiveData
    }

    fun checkAuthentication(mContext : Context,
                            userNameText: String?,
                            passwordText: String?
    )
    {

        if (userNameText.equals(
                "",
                ignoreCase = true
            )
        ) setAlert(
            mContext.getString(R.string.Login_Error),
            mContext.getString(R.string.please_enter_name),
            false
        ) else if (passwordText.equals(
                "",
                ignoreCase = true
            )
        ) setAlert(
            mContext.getString(R.string.Login_Error),
            mContext.getString(R.string.please_enter_password),
            false
        )else{
            val loginModel : LoginRequestModel = LoginRequestModel()
            loginModel.MemberLogin = userNameText
            loginModel.Password = passwordText
            loginModel.DeviceId = "mobile"


          checkAuthentication(loginModel,mContext)

        }
    }

    private fun checkAuthentication(
        loginRequestModel: LoginRequestModel , mContext : Context
    ) {
        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().checkAuthentication(
                loginRequestModel,

                object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {

                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog

                        try {


                            val loginResponseModel: LoginResponseModel? = response.body() as LoginResponseModel?
                            if (loginResponseModel?.statusCode == 200) {
                                if (response.body() != null) {

                                    loginResponseModel.objListMemberLoginServiceModel.status = true
                                    mSessionManager.setUserDetailModel(loginResponseModel.objListMemberLoginServiceModel)
                                    mSessionManager.setAuthorization("bearer "+loginResponseModel.accessToken )
                                    loginliveData.value = loginResponseModel
                                }

                            } else  if (loginResponseModel?.statusCode  == 401){

                                setAlert(
                                    mContext.getString(R.string.Login_Error),
                                    mContext.getString(R.string.sorry_user_loggedin_otherdevice),
                                    false
                                )
                            }
                            else  if (loginResponseModel?.statusCode  == 404){

                                setAlert(
                                    mContext.getString(R.string.Login_Error),
                                    mContext.getString(R.string.Error_from_Server),
                                    false
                                )
                            }

                            else{

                                setAlert(
                                    mContext.getString(R.string.Login_Error),
                                    mContext.getString(R.string.sorry_invalid_user_name_email),
                                    false
                                )
                            }


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


                    }

                    override fun onFailure() {
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
