package com.aadya.whiskyapp.profile.upload

import com.aadya.whiskyapp.profile.model.UploadResponse
import com.aadya.whiskyapp.utils.CommonUtils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyAPI {

    @Multipart
    @POST("Mobile/UpdateAppMemberPhoto")
    fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part,
        @Part("MemberId") desc: Int
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): MyAPI {
            return Retrofit.Builder()
                .baseUrl(CommonUtils.APIURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyAPI::class.java)
        }
    }
}