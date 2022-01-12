package com.aadya.whiskyapp.payment.model

import com.google.gson.annotations.SerializedName

 class PaymentByEmailResponse {
     @SerializedName("clientSecret")
     var clientSecret: String? = null
 }
