package com.aadya.whiskyapp.retrofit

import com.aadya.gist.login.model.ReserveRequestModel
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.profile.model.ProfileEditRequestModel
import com.aadya.whiskyapp.landing.model.LoginResponseModel
import com.aadya.whiskyapp.events.model.RSVPRequestModel
import com.aadya.whiskyapp.events.model.RSVPResponseModel
import com.aadya.whiskyapp.landing.model.LoginRequestModel
import com.aadya.whiskyapp.profile.model.ProfileRequestModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.purchasehistory.model.PurchaseHistory
import com.aadya.whiskyapp.reserve.model.ReserveResponseModel
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
}