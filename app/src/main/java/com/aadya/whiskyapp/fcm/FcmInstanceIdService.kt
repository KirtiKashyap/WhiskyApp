package com.aadya.whiskyapp.fcm

import android.content.ContentValues.TAG
import android.util.Log
import com.aadya.whiskyapp.utils.SessionManager
import com.google.firebase.messaging.FirebaseMessagingService


class FcmInstanceIdService : FirebaseMessagingService() {
    private lateinit var mSessionManager: SessionManager
    override fun onNewToken(token: String) {
        mSessionManager = SessionManager.getInstance(application)!!
        Log.d(TAG, "Refreshed token: $token")
    }

}