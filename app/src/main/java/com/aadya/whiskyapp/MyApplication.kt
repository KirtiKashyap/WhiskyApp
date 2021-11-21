package com.aadya.whiskyapp

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51Jlt4ySAToMq1oS5dtA0kVj5d5nmZV7pzAaiucGkO2CFPqHuYz22tmHoNKPQaFtbLNDwYZh7lKc9JQRAAv7gHuJT00NRFz273R"
        )
    }
    companion object{
        var isEventDialogOpen=true
        var isSpecialEventDialogOpen=true
    }
}