package com.aadya.whiskyapp.payment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aadya.whiskyapp.R
import kotlinx.android.synthetic.main.activity_payment_success.*

class PaymentSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)
        backButton.setOnClickListener {
            finish()
        }
    }
}