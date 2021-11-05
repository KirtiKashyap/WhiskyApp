package com.aadya.whiskyapp.profile.upload
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Header("Authorization")authHeader: String,
        @Part image: MultipartBody.Part,
        @Part("MemberId") desc: RequestBody
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): MyAPI {
            return Retrofit.Builder()
                .baseUrl("http://92.204.128.4:5002/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyAPI::class.java)
        }
    }
}