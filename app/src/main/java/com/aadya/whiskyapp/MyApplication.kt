package com.aadya.whiskyapp

import android.app.Application
import com.stripe.android.PaymentConfiguration
import kotlin.properties.Delegates

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51JgvGWHg3vu6Xtwj7pamp4M0EWab892xZ5oFuXem3Wz7iMpthh1W22FxdJ2vVuOHkjH4yz0kje34k0yJBQk38aOL00pOB49p3n"
        )
        /*PaymentConfiguration.init(
            applicationContext,
            "pk_test_51KEGY3Iz42LzNrtBrJtPmVxE7arQgmb10nSgcS436hsGk26KQqxdWxhtP0PbbrYrwvX4oa2HCpV3haeznKNpAjRf00CZKECNix"
        )*/
    }

    companion object{
        @JvmStatic
        var isEventDialogOpen=true
        @JvmStatic
        var isSpecialEventDialogOpen=true
        @JvmStatic
        var mSelectedGuestPass=0
        @JvmStatic
        var isPlusOne=false
    }
}