package com.aadya.whiskyapp.retrofit

import com.aadya.gist.login.model.LoginRequestModel
import com.aadya.gist.login.model.ReserveRequestModel
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.events.model.RSVPRequestModel
import com.aadya.whiskyapp.events.model.RSVPResponseModel
import com.aadya.whiskyapp.landing.model.LoginResponseModel
import com.aadya.whiskyapp.profile.model.ProfileEditRequestModel
import com.aadya.whiskyapp.profile.model.ProfileEditResponseModel
import com.aadya.whiskyapp.profile.model.ProfileRequestModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.reserve.model.ReserveResponseModel
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.CommonUtils
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APICallService {

    @POST(CommonUtils.APIURL.RSVP)
    fun RSVP(
        @Header("Authorization") authHeader: String?,
        @Body mRSVPRequestModel: RSVPRequestModel
    ): Call<RSVPResponseModel?>?

    @POST(CommonUtils.APIURL.Profile)
    fun Profile(
        @Header("Authorization") authHeader: String?,
        @Body mProfileRequestModel: ProfileRequestModel
    ): Call<ProfileResponseModel?>?

    @GET(CommonUtils.APIURL.EventAttending)
    fun getEventAttendingList(@Header("Authorization") authHeader: String?): Call<List<EventsResponseModel?>?>

    @GET(CommonUtils.APIURL.EventAttending)
    fun getPurchaseList(@Header("Authorization") authHeader: String?): Call<List<SpecialOfferResponseModel?>?>


    @GET(CommonUtils.APIURL.SpecialOffer)
    fun getSpecialOfferList(@Header("Authorization") authHeader: String?): Call<List<SpecialOfferResponseModel?>?>

    @POST(CommonUtils.APIURL.LOGIN_USER)
    fun loginUser(@Body loginRequestModel: LoginRequestModel?): Call<LoginResponseModel>?

    @POST(CommonUtils.APIURL.Reserve)
    fun reserve(@Header("Authorization") authHeader: String?,@Body reserveRequestModel: ReserveRequestModel?): Call<ReserveResponseModel>?

    @POST(CommonUtils.APIURL.FORGOT_PASSWORD)
    fun forgotPassword(@Body loginRequestModel: LoginRequestModel?): Call<Int?>?

    @GET(CommonUtils.APIURL.Events)
    fun getEventsList (@Header("Authorization") authHeader: String?): Call<List<EventsResponseModel?>?>

    @POST(CommonUtils.APIURL.ProfileEdit)
    fun ProfileEdit(
        @Header("Authorization") authHeader: String?,
        @Body mProfileRequestModel: ProfileEditRequestModel?
    ): Call<ProfileEditResponseModel?>?
}