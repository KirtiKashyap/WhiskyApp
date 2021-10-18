package com.aadya.whiskyapp.events.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class RSVPRequestModel() {



    @SerializedName("EventID")
    var EventID: Int? = null

    @SerializedName("EventFeedbackID")
    var EventFeedbackID: Int? = null




}