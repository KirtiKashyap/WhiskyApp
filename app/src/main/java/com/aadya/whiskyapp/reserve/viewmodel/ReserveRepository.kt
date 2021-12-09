package com.aadya.whiskyapp.reserve.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.reserve.model.*
import com.aadya.whiskyapp.retrofit.APIResponseListener
import com.aadya.whiskyapp.retrofit.RetrofitService
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.Connection
import retrofit2.Response

class ReserveRepository(application: Application) {
    private var application : Application = application
    private var mCommonUtils : CommonUtils = CommonUtils
    private val reserveliveData: MutableLiveData<ReserveResponseModel?> = MutableLiveData<ReserveResponseModel?>()
    private val reserveInfoLiveData: MutableLiveData<ReserveInfoResponse?> = MutableLiveData<ReserveInfoResponse?>()
    private val cancelReservation: MutableLiveData<Int?> = MutableLiveData<Int?>()
    private val progressLiveData = MutableLiveData<Int>()
    private val alertLiveData: MutableLiveData<AlertModel> = MutableLiveData<AlertModel>()

    private var reserveUnAuthorizedLiveData = MutableLiveData<Boolean>()

    fun getreserveUnAuthorized(): MutableLiveData<Boolean> {
        return reserveUnAuthorizedLiveData
    }

    fun getReserveViewState(): MutableLiveData<ReserveResponseModel?>? {
        return reserveliveData
    }

    fun getReserveInfoViewState(): MutableLiveData<ReserveInfoResponse?>? {
        return reserveInfoLiveData
    }
    fun getCancelReservationViewState(): MutableLiveData<Int?>? {
        return cancelReservation
    }
    fun getProgressState(): MutableLiveData<Int>? {
        return progressLiveData
    }

    fun getAlertViewState(): MutableLiveData<AlertModel>? {
        return alertLiveData
    }

    fun checkReserveValidation(
        mContext: Context,
        what_u_want_to_eat: String?,
        date: String?, time: String?, no_of_people: String?, userId: Int?, authorization: String?
    )
    {

        if ((what_u_want_to_eat==null)||what_u_want_to_eat.trim().equals(
                "",
                ignoreCase = true)
        ) setAlert(
            mContext.getString(R.string.reserve_Error),
            mContext.getString(R.string.please_enter_what_u_want_to_eat),
            false
        ) else if (date.equals(
                "",
                ignoreCase = true
            )
        ) setAlert(
            mContext.getString(R.string.reserve_Error),
            mContext.getString(R.string.please_select_date),
            false
        )else if (time.equals(
                "",
                ignoreCase = true
            )
        ) setAlert(
            mContext.getString(R.string.reserve_Error),
            mContext.getString(R.string.please_select_time),
            false
        )else if (no_of_people.equals(
                "",
                ignoreCase = true
            )
        ) setAlert(
            mContext.getString(R.string.reserve_Error),
            mContext.getString(R.string.please_select_no_of_people),
            false
        )else{
            val reserveRequestModel = ReserveRequestModel()
            reserveRequestModel.Favorite = what_u_want_to_eat
            reserveRequestModel.BookingDate = date
            reserveRequestModel.BookingTime =time
            reserveRequestModel.NumberofPeople =no_of_people
            reserveRequestModel.BookingInfoID = 0
            reserveRequestModel.Title = "null"
            reserveRequestModel.Description = "null"
            reserveRequestModel.UserID=userId
          checkReserveValidation(reserveRequestModel,mContext,authorization)

        }
    }

    private fun checkReserveValidation(
        reserveRequestModel: ReserveRequestModel, mContext: Context, authorization: String?
    ) {
        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().checkReserveValidation(authorization,
                reserveRequestModel,

                object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {

                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog

                        try {
                            val reserveResponseModel: ReserveResponseModel? = response.body() as ReserveResponseModel?

                            if(response.code() == 401)
                                reserveUnAuthorizedLiveData.value = true

                            else if(response.code() == 404)
                                setAlert(
                                    mContext.getString(R.string.reserve_Error),
                                    mContext.getString(R.string.Error_from_Server),
                                    false
                                )

                            else if(response.code() == 200)
                                reserveliveData.value = reserveResponseModel

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


    fun getReservationInfo(
        mContext: Context, reserveInfoRequest: ReserveInfoRequest,authorization: String?
    ) {
        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().getReserveInfo(authorization,
                reserveInfoRequest,

                object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {

                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog

                        try {
                            val reserveResponse: ReserveInfoResponse? = response.body() as ReserveInfoResponse?

                            if(response.code() == 401)
                                reserveUnAuthorizedLiveData.value = true

                            else if(response.code() == 404)
                                setAlert(
                                    mContext.getString(R.string.reserve_Error),
                                    mContext.getString(R.string.Error_from_Server),
                                    false
                                )

                            else if(response.code() == 200)
                                reserveInfoLiveData.value = reserveResponse

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

    fun getCancelReservation(
        mContext: Context, cancelReservationRequest : CancelReservationRequest,authorization: String?
    ) {
        if (Connection.instance?.isNetworkAvailable(application) == true) {
            progressLiveData.value = CommonUtils.ProgressDialog.showDialog
            RetrofitService().getCancelReservation(authorization,
                cancelReservationRequest,

                object : APIResponseListener {
                    override fun onSuccess(response: Response<Any>) {

                        progressLiveData.value = CommonUtils.ProgressDialog.dismissDialog

                        try {
                            val reserveResponse: Int? = response.body() as Int?

                            if(response.code() == 401)
                                reserveUnAuthorizedLiveData.value = true

                            else if(response.code() == 404)
                                setAlert(
                                    mContext.getString(R.string.reserve_Error),
                                    mContext.getString(R.string.Error_from_Server),
                                    false
                                )

                            else if(response.code() == 200)
                                cancelReservation.value = reserveResponse

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


}
