package com.aadya.whiskyapp.landing.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.dashboard.ui.DashBoardActivity
import com.aadya.whiskyapp.databinding.ActivityLandingBinding
import com.aadya.whiskyapp.databinding.MainHeaderBinding
import com.aadya.whiskyapp.landing.model.LoginResponseModel
import com.aadya.whiskyapp.landing.viewmodel.LoginFactory
import com.aadya.whiskyapp.landing.viewmodel.LoginViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager


class LandingActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityLandingBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderBinding
    private lateinit var  loginViewModel: LoginViewModel
    private lateinit var mCommonUtils : CommonUtils
    private lateinit var mSessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intializeMembers()
        handleClickListner()
        handleObservers()
    }

    private fun handleObservers() {
        loginViewModel.getLoginViewState()?.observe(this,
            object : Observer<LoginResponseModel?> {
                override fun onChanged(loginResponseModel: LoginResponseModel?) {
                    if (loginResponseModel == null) return
                    val intent = Intent(this@LandingActivity, DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })


        loginViewModel.getAlertViewState()?.observe(this,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {
                    if (alertModel == null) return
                    mCommonUtils.showAlert(
                        alertModel.duration,
                        alertModel.title,
                        alertModel.message,
                        alertModel.drawable,
                        alertModel.color,
                        this@LandingActivity

                    )
                }
            })


        loginViewModel.getProgressState()?.observe(this,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), this@LandingActivity
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })
    }

    private fun handleClickListner() {

        mBinding.loginButton.setOnClickListener {
          loginViewModel.checkAuthentication(
                this@LandingActivity,
                mBinding.usernameedittext.text.toString(),
                mBinding.passwordedittext.text.toString()
            )
        }

        mBinding.forgotButton.setOnClickListener {
            val intent = Intent(this@LandingActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


    }



    private fun intializeMembers() {
        mCommonUtils = CommonUtils
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_landing)
        mCommonUtils.setButtonFont(mBinding.loginButton,this)
        mCommonUtils.setButtonFont(mBinding.forgotButton,this)
        mSessionManager = SessionManager.getInstance(this@LandingActivity)!!
        loginViewModel = ViewModelProviders.of(
            this, LoginFactory(
                application, mSessionManager
            )
        ).get(
            LoginViewModel::class.java
        )





    }



}