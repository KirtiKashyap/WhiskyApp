package com.aadya.whiskyapp.profile.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.MyApplication
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.clublocation.ui.LocationFragment
import com.aadya.whiskyapp.databinding.*
import com.aadya.whiskyapp.events.eventdailoge.EventsLaunchDialogFragment
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.profile.viewmodel.ProfileFactory
import com.aadya.whiskyapp.profile.viewmodel.ProfileViewModel
import com.aadya.whiskyapp.specialoffers.specialofferdialog.SpecialOfferDialogFragment
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.secretcode_new_layout.*


class SecretCodeFragment : Fragment() {


    private lateinit var mBinding: SecretcodeNewLayoutBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mSessionManager: SessionManager
    private lateinit var mProfileViewModel : ProfileViewModel
    private lateinit var mCommonUtils : CommonUtils


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)

        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSessionManager = SessionManager.getInstance(context)!!
        mProfileViewModel = ViewModelProvider(this, ProfileFactory(activity?.application)).get(
            ProfileViewModel::class.java
        )
        mProfileViewModel.getProfile(
            mSessionManager.getAuthorization(),
            mSessionManager.getUserDetailLoginModel()?.memberID
        )
        handleObserver()
    }



    private fun handleObserver() {
        mProfileViewModel.getProfileObserver().observe(this, Observer {
            if (it == null) return@Observer
            mSessionManager.setProfileModel(it)
            lastseen.text="Last "+mCommonUtils.getWeekDay(it.userLoginTime)
            val qrCode = it.qrCode
            context?.let {
                Glide.with(it)
                    .load(CommonUtils.APIURL.QRCode_IMAGE_URL +qrCode)
                    .into(img_secretcode)
            }
            if(it.isEvent && MyApplication.isEventDialogOpen){
                EventsLaunchDialogFragment.newInstance().show(activity?.supportFragmentManager!!, EventsLaunchDialogFragment.TAG)
            }
            /*if(it.isSpecial && MyApplication.isSpecialEventDialogOpen){
                SpecialOfferDialogFragment.newInstance().show(activity?.supportFragmentManager!!, SpecialOfferDialogFragment.TAG)
            }*/



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
                requireActivity()

            )

            Handler().postDelayed({
                val intent = Intent(requireActivity(), LandingActivity::class.java)
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
                        requireActivity()

                    )
                }
            })


        mProfileViewModel.getProgressState()?.observe(this,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), requireContext()
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })
    }


    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {

        mCommonUtils = CommonUtils
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.secretcode_new_layout,
            container,
            false
        )

        setIncludedLayout()
        handleClickListner()



    }

    private fun handleClickListner() {
        mBinding.imgWhereisidplace.setOnClickListener {
            launchFragment(LocationFragment.newInstance(), "LocationFragment")
        }

    }

    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
        }

        mIncludedLayoutBinding.imgLogo.setOnClickListener {

            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }

    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            SecretCodeFragment().apply {

            }
    }
}