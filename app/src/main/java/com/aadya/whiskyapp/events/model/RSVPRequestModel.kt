package com.aadya.whiskyapp.events.model

import com.google.gson.annotations.SerializedName

class RSVPRequestModel() {



    @SerializedName("EventID")
    var EventID: Int? = null

    @SerializedName("EventFeedbackID")
    var EventFeedbackID: Int? = null

    @SerializedName("RemainingGuestPasses")
    var remainingGuestPasses: Int? = null

    @SerializedName("PlusOne")
    var PlusOne: Boolean? = null

}