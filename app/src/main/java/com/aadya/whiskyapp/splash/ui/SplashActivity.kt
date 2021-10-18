package com.aadya.whiskyapp.splash.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.dashboard.ui.DashBoardActivity
import com.aadya.whiskyapp.databinding.ActivitySplashNewBinding
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.splash.viewmodel.SplashViewModel
import com.aadya.whiskyapp.utils.SessionManager
import com.bumptech.glide.Glide


class SplashActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySplashNewBinding
    private lateinit var mSessionManager: SessionManager
    private lateinit var splashViewModel : SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intializeMembers()
        handleobserver()


    }


    private fun handleobserver() {

        splashViewModel.splashViewState.observe(this, Observer { aBoolean ->
            if (aBoolean == null) return@Observer

            if( mSessionManager.getUserDetailLoginModel()?.status == true){
                val intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this@SplashActivity, LandingActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        splashViewModel.checkSplashActivityState()
    }

    private fun intializeMembers() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_new)
        mSessionManager = SessionManager.getInstance(application)!!
         splashViewModel = ViewModelProviders.of(this).get(
            SplashViewModel::class.java
        )
    }

}