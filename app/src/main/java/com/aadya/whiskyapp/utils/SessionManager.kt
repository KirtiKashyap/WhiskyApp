package com.aadya.whiskyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.aadya.whiskyapp.landing.model.ObjListUserLoginServiceModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.example.example.ObjListMemberLoginServiceModel
import com.google.gson.Gson


class SessionManager {


    companion object {
        const val PREF_NAME = "whiskey"
        const val TAG_USERDETAILMODLE_PREF = "UserDetail"
        const val TAG_PROFILEMODLE_PREF = "ProfileModel"
        private const val AUTHORIZATION = "authorization"
        private const val TAG_ECMTOKEN_PREF = "token"


        private var sessionManager: SessionManager? = null
        private var sharedPreferences: SharedPreferences? = null
        private var context: Context? = null
        private var editor: SharedPreferences.Editor? = null
        fun getInstance(context1: Context?): SessionManager? {
            context = context1
            if (sessionManager == null) {
                sessionManager = SessionManager()
                sharedPreferences = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

            }
            return sessionManager
        }
    }


    fun setUserDetailModel(userDetailModel: ObjListMemberLoginServiceModel) {
        val edit: SharedPreferences.Editor? = sharedPreferences?.edit()
        val gson = Gson()
        val paidObject = gson.toJson(userDetailModel)
        edit?.putString(TAG_USERDETAILMODLE_PREF, paidObject)
        edit?.commit()

    }

    fun getUserDetailLoginModel(): ObjListMemberLoginServiceModel? {
        val userObjectString: String? = sharedPreferences?.getString(
            TAG_USERDETAILMODLE_PREF, ""
        )
        val gson = Gson()
        val signUpRequestModel : ObjListMemberLoginServiceModel? = gson.fromJson(
            userObjectString,
            ObjListMemberLoginServiceModel::class.java
        )

        return signUpRequestModel
    }

    fun getAuthorization(): String? {
        val edit: SharedPreferences.Editor? = sharedPreferences?.edit()
        return sharedPreferences?.getString(AUTHORIZATION, "")
    }

    fun setAuthorization(authorization: String?) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(AUTHORIZATION, authorization)
        editor?.apply()
    }

    fun setProfileModel(profileModel: ProfileResponseModel) {
        val edit: SharedPreferences.Editor? = sharedPreferences?.edit()
        val gson = Gson()
        val paidObject = gson.toJson(profileModel)
        edit?.putString(TAG_PROFILEMODLE_PREF, paidObject)
        edit?.commit()

    }

    fun getProfileModel(): ProfileResponseModel? {
        val userObjectString: String? = sharedPreferences?.getString(
            TAG_PROFILEMODLE_PREF, ""
        )
        val gson = Gson()
        val profileModel : ProfileResponseModel? = gson.fromJson(
            userObjectString,
            ProfileResponseModel::class.java
        )

        return profileModel
    }

    fun getFCMToken(): String? {
        return sharedPreferences?.getString(TAG_ECMTOKEN_PREF, "")
    }

    fun setFCMToken(token: String?) {
        editor = sharedPreferences?.edit()
        editor?.putString(TAG_ECMTOKEN_PREF, token)
        editor?.apply()
    }


    fun clearpreference(tag: String?, mContext: Context) {
         var editor: SharedPreferences.Editor? = null
        var sharedPreferences: SharedPreferences? = null
        sharedPreferences = mContext.getSharedPreferences(
            PREF_NAME, Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
        editor.putString(tag, "")
        editor.remove(tag).apply()
    }
}