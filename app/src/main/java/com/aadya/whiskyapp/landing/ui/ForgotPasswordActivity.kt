package com.aadya.whiskyapp.landing.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.ActivityForgotPasswordBinding
import com.aadya.whiskyapp.landing.viewmodel.ForgotPasswordFactory
import com.aadya.whiskyapp.landing.viewmodel.ForgotPasswordViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityForgotPasswordBinding
    private lateinit var  forgotPasswordViewModel: ForgotPasswordViewModel
    private lateinit var mCommonUtils : CommonUtils
    private lateinit var mSessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intializeMembers()
        handleClickListner()
        handleObservers()
    }

    private fun handleClickListner() {
        mBinding.forgotButton.setOnClickListener {
            forgotPasswordViewModel.checkAuthentication(this,mBinding.usernameedittext.text.toString())
        }

        mBinding.backToLoginButton.setOnClickListener{
            mCommonUtils.LogMessage("Clicked")
            this.finish()
        }
    }


    private fun handleObservers() {

        forgotPasswordViewModel.getForgotPasswordViewState()?.observe(this,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {
                    if (alertModel == null) return
                    mCommonUtils.showAlert(
                        alertModel.duration,
                        alertModel.title,
                        alertModel.message,
                        alertModel.drawable,
                        alertModel.color,
                        this@ForgotPasswordActivity

                    )
                }
            })




        forgotPasswordViewModel.getAlertViewState()?.observe(this,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {
                    if (alertModel == null) return
                    mCommonUtils.showAlert(
                        alertModel.duration,
                        alertModel.title,
                        alertModel.message,
                        alertModel.drawable,
                        alertModel.color,
                        this@ForgotPasswordActivity

                    )
                }
            })


        forgotPasswordViewModel.getProgressState()?.observe(this,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), this@ForgotPasswordActivity
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })
    }

    private fun intializeMembers() {
        mCommonUtils = CommonUtils
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        mSessionManager = SessionManager.getInstance(this@ForgotPasswordActivity)!!


        mCommonUtils.setButtonFont(mBinding.forgotButton,this)
        mCommonUtils.setButtonFont(mBinding.backToLoginButton,this)
        forgotPasswordViewModel = ViewModelProviders.of(
            this, ForgotPasswordFactory(
                application
            )
        ).get(
            ForgotPasswordViewModel::class.java
        )

    }

    fun setButtonFont(mbutton : Button, context: Context) {
        mbutton.typeface = ResourcesCompat.getFont(
            context,
            R.font.archivonarrow_regular
        );
    }


}