package com.aadya.whiskyapp.retrofit

import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.profile.model.ProfileEditRequestModel
import com.aadya.whiskyapp.landing.model.LoginResponseModel
import com.aadya.whiskyapp.events.model.RSVPRequestModel
import com.aadya.whiskyapp.events.model.RSVPResponseModel
import com.aadya.whiskyapp.landing.model.LoginRequestModel
import com.aadya.whiskyapp.menu.model.MenuResponse
import com.aadya.whiskyapp.payment.model.PaymentByEmail
import com.aadya.whiskyapp.payment.model.PaymentByEmailResponse
import com.aadya.whiskyapp.payment.model.PaymentResponse
import com.aadya.whiskyapp.payment.model.PaymentUpdate
import com.aadya.whiskyapp.profile.model.ProfileRequestModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.purchasehistory.model.PurchaseHistory
import com.aadya.whiskyapp.reserve.model.*
import com.aadya.whiskyapp.scanlog.model.ScanLogRequest
import com.aadya.whiskyapp.scanlog.model.ScanLogResponse
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitService {

    fun getProfileEdit(
        authorization: String,
        profilerequestModel: ProfileEditRequestModel,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<ProfileResponseModel> = service.ProfileEdit(authorization,profilerequestModel)
        call?.enqueue(object : Callback<ProfileResponseModel> {
            override fun onResponse(
                call: Call<ProfileResponseModel>,
                response: Response<ProfileResponseModel?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<ProfileResponseModel>, t: Throwable) {

                apiResponseListener.onFailure()
            }
        })
    }

    fun getProfile(
        authorization: String?,
        profilerequestModel: ProfileRequestModel,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<ProfileResponseModel?>? = service.Profile(authorization,profilerequestModel)
        call?.enqueue(object : Callback<ProfileResponseModel?> {
            override fun onResponse(
                call: Call<ProfileResponseModel?>,
                response: Response<ProfileResponseModel?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<ProfileResponseModel?>, t: Throwable) {

                apiResponseListener.onFailure()
            }
        })
    }




    fun getEventNotification(
        authorization: String?,
        profilerequestModel: ProfileRequestModel,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<Any>? = service.EvenetNotification(authorization,profilerequestModel)
        call?.enqueue(object : Callback<Any?> {
            override fun onResponse(
                call: Call<Any?>,
                response: Response<Any?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {

                apiResponseListener.onFailure()
            }
        })
    }

    fun getSpecialOfferNotification(
        authorization: String?,
        profilerequestModel: ProfileRequestModel,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<Any?>? = service.OfferNotification(authorization,profilerequestModel)
        call?.enqueue(object : Callback<Any?> {
            override fun onResponse(
                call: Call<Any?>,
                response: Response<Any?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {

                apiResponseListener.onFailure()
            }
        })
    }



    fun getRSVP(
        authorization: String?,
        rsvprequestModel: RSVPRequestModel,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<RSVPResponseModel?>? = service.RSVP(authorization,rsvprequestModel)
        call?.enqueue(object : Callback<RSVPResponseModel?> {
            override fun onResponse(
                call: Call<RSVPResponseModel?>,
                response: Response<RSVPResponseModel?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<RSVPResponseModel?>, t: Throwable) {

                apiResponseListener.onFailure()
            }
        })
    }


    fun getEventAttendingList(authorization: String?,apiResponseListener: APIResponseListener) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<List<EventsResponseModel?>?> = service.getEventAttendingList(authorization)
        call?.enqueue(object : Callback<List<EventsResponseModel?>?> {


            override fun onResponse(
                call: Call<List<EventsResponseModel?>?>,
                response: Response<List<EventsResponseModel?>?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<List<EventsResponseModel?>?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }

    fun getPurchaseHistoryList(authorization: String?,apiResponseListener: APIResponseListener) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<List<PurchaseHistory?>?> = service.getPurchaseList(authorization)
        call?.enqueue(object : Callback<List<PurchaseHistory?>?> {
            override fun onResponse(
                call: Call<List<PurchaseHistory?>?>,
                response: Response<List<PurchaseHistory?>?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<List<PurchaseHistory?>?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }


    fun getSpecialOffer(authorization: String?,apiResponseListener: APIResponseListener) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<List<SpecialOfferResponseModel?>?> = service.getSpecialOfferList(authorization)
        call?.enqueue(object : Callback<List<SpecialOfferResponseModel?>?> {


            override fun onResponse(
                call: Call<List<SpecialOfferResponseModel?>?>,
                response: Response<List<SpecialOfferResponseModel?>?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<List<SpecialOfferResponseModel?>?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }


    fun getEvents(authorization: String?,apiResponseListener: APIResponseListener) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<List<EventsResponseModel?>?> = service.getEventsList(authorization)
        call?.enqueue(object :Callback<List<EventsResponseModel?>?>{
            override fun onResponse(
                call: Call<List<EventsResponseModel?>?>,
                response: Response<List<EventsResponseModel?>?>
            ) {
              apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<List<EventsResponseModel?>?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }

    fun getMenuData(authorization: String?,apiResponseListener: APIResponseListener) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<MenuResponse?> = service.getMenuData(authorization)
        call?.enqueue(object :Callback<MenuResponse?>{
            override fun onResponse(
                call: Call<MenuResponse?>,
                response: Response<MenuResponse?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<MenuResponse?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }

    fun getScanLog(
        authorization: String,
        scanLogRequest: ScanLogRequest,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<List<ScanLogResponse?>?>  = service.getScanLog(authorization,scanLogRequest)
        call?.enqueue(object :Callback<List<ScanLogResponse?>?>{
            override fun onResponse(
                call: Call<List<ScanLogResponse?>?>,
                response: Response<List<ScanLogResponse?>?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<List<ScanLogResponse?>?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })

    }


    fun getReserveHistoryLog(
        authorization: String,
        reserveInfoRequest: ReserveInfoRequest,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<List<ReserveInfoResponse?>?>  = service.getReserveHistoryLog(authorization)
        call?.enqueue(object :Callback<List<ReserveInfoResponse?>?>{
            override fun onResponse(
                call: Call<List<ReserveInfoResponse?>?>,
                response: Response<List<ReserveInfoResponse?>?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<List<ReserveInfoResponse?>?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })

    }


    fun checkReserveValidation(
        authorization: String?,
        reserveRequestModel: ReserveRequestModel?,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<ReserveResponseModel>? = service.reserve(authorization,reserveRequestModel)
        call?.enqueue(object : Callback<ReserveResponseModel?> {
            override fun onResponse(
                call: Call<ReserveResponseModel?>,
                response: Response<ReserveResponseModel?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<ReserveResponseModel?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }



    fun getReserveInfo(
        authorization: String?,
        reserveRequestModel: ReserveInfoRequest?,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<ReserveInfoResponse>? = service.reserveInfo(authorization,reserveRequestModel)
        call?.enqueue(object : Callback<ReserveInfoResponse?> {
            override fun onResponse(
                call: Call<ReserveInfoResponse?>,
                response: Response<ReserveInfoResponse?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<ReserveInfoResponse?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }

    fun getCancelReservation(
        authorization: String?,
        cancelReservationRequest: CancelReservationRequest?,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<Int>? = service.cancelReservation(authorization,cancelReservationRequest)
        call?.enqueue(object : Callback<Int?> {
            override fun onResponse(
                call: Call<Int?>,
                response: Response<Int?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<Int?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }


    fun checkAuthentication(
        loginRequestModel: LoginRequestModel?,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<LoginResponseModel>? = service.loginUser(loginRequestModel)
        call?.enqueue(object : Callback<LoginResponseModel?> {
            override fun onResponse(
                call: Call<LoginResponseModel?>,
                response: Response<LoginResponseModel?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<LoginResponseModel?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }

    fun checkForgotAuthentication(
        loginRequestModel: LoginRequestModel?,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<Int?>? = service.forgotPassword(loginRequestModel)
        call?.enqueue(object : Callback<Int?> {
            override fun onResponse(
                call: Call<Int?>,
                response: Response<Int?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<Int?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }

    fun paymentUpdate(
        authorization: String?,
        paymentRequestModel: PaymentUpdate,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<PaymentResponse?>? = service.paymentUpdate(authorization,paymentRequestModel)
        call?.enqueue(object : Callback<PaymentResponse?> {
            override fun onResponse(
                call: Call<PaymentResponse?>,
                response: Response<PaymentResponse?>
            ) {
                apiResponseListener.onSuccess(response)
            }

            override fun onFailure(call: Call<PaymentResponse?>, t: Throwable) {
                apiResponseListener.onFailure()
            }
        })
    }

    fun paymentByEmail(
        authorization: String?,
        paymentByEmail: PaymentByEmail,
        apiResponseListener: APIResponseListener
    ) {
        val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
        val call: Call<PaymentByEmailResponse?>? = service.paymentByEmail(authorization,paymentByEmail)
        call?.enqueue(object : Callback<PaymentByEmailResponse?> {

            override fun onFailure(call: Call<PaymentByEmailResponse?>, t: Throwable) {
                apiResponseListener.onFailure()
            }

            override fun onResponse(
                call: Call<PaymentByEmailResponse?>,
                response: Response<PaymentByEmailResponse?>
            ) {
                apiResponseListener.onSuccess(response)
            }
        })
    }
}