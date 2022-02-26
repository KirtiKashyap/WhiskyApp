package com.aadya.whiskyapp.payment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.profile.viewmodel.ProfileFactory
import com.aadya.whiskyapp.profile.viewmodel.ProfileViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager
import kotlinx.android.synthetic.main.activity_payment_success.*
import kotlinx.android.synthetic.main.secretcode_new_layout.*

class PaymentSuccessActivity : AppCompatActivity() {
    private lateinit var mSessionManager: SessionManager
    private lateinit var mProfileViewModel : ProfileViewModel
    private lateinit var mCommonUtils : CommonUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(this)!!
        mProfileViewModel = ViewModelProvider(this, ProfileFactory(application)).get(
            ProfileViewModel::class.java
        )

        handleObserver()
        backButton.setOnClickListener {
            if(intent.getStringExtra("activityCheck").equals("CheckOut")) {
                mProfileViewModel.getProfile(
                    mSessionManager.getAuthorization(),
                    mSessionManager.getUserDetailLoginModel()?.memberID
                )
            }else{
                finish()
            }

        }
    }

    private fun handleObserver() {
        mProfileViewModel.getProfileObserver().observe(this, Observer {
            if (it == null) return@Observer
            mSessionManager.setProfileModel(it)
            finish()
        })


        mProfileViewModel.getprofileUnAuthorized().observe(this, Observer {
            val alertModel = AlertModel(
                2000, resources.getString(R.string.login_error),
                resources.getString(R.string.please_login)
                , R.drawable.wrong_icon, R.color.notiFailColor
            )
            mCommonUtils.showAlert(
                alertModel.duration,
                alertModel.title,
                alertModel.message,
                alertModel.drawable,
                alertModel.color,
                this@PaymentSuccessActivity

            )

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@PaymentSuccessActivity, LandingActivity::class.java)
                startActivity(intent)}, 2000)

        })

        mProfileViewModel.getAlertViewState()?.observe(this,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {
                    if (alertModel == null) return
                    mCommonUtils.showAlert(
                        alertModel.duration,
                        alertModel.title,
                        alertModel.message,
                        alertModel.drawable,
                        alertModel.color,
                        this@PaymentSuccessActivity

                    )
                }
            })


        mProfileViewModel.getProgressState()?.observe(this,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), this@PaymentSuccessActivity
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })
    }
}