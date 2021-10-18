package com.aadya.whiskyapp.profile.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.*
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.profile.model.ProfileEditRequestModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.profile.viewmodel.ProfileEditFactory
import com.aadya.whiskyapp.profile.viewmodel.ProfileEditViewModel
import com.aadya.whiskyapp.profile.viewmodel.ProfileFactory
import com.aadya.whiskyapp.profile.viewmodel.ProfileViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager


class ProfileEditFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileEditBinding
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    private var mProfileModel : ProfileResponseModel? = null
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mDOBLayoutBinding: DoblayoutProfileeditBinding
    private lateinit var mPhoneLayoutBinding: PhonelayoutProfileeditBinding
    private lateinit var mEmailLayoutBinding: EmaillayoutProfileeditBinding
    private lateinit var mAddressLayoutBinding: AddresslayoutProfileeditBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mProfileEditViewModel : ProfileEditViewModel
    private lateinit var mProfileViewModel : ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)


        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
        }
        mIncludedLayoutBinding.imgLogo.setOnClickListener {

            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }

        mDOBLayoutBinding = mBinding.doblayoutProfileedit
        mPhoneLayoutBinding = mBinding.phonelayoutProfileedit
        mEmailLayoutBinding = mBinding.emaillayoutProfileedit
        mAddressLayoutBinding = mBinding.addresslayoutProfileedit
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {


        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile_edit,
            container,
            false
        )

        mCommonUtils = CommonUtils
        setIncludedLayout()
        mSessionManager = SessionManager.getInstance(requireContext())!!
        mProfileModel = mSessionManager.getProfileModel()
        setUIValues()
        handleClickListner()
        mProfileEditViewModel = ViewModelProvider(this, ProfileEditFactory(activity?.application)).get(
            ProfileEditViewModel::class.java
        )
        mProfileViewModel = ViewModelProvider(this, ProfileFactory(activity?.application)).get(
            ProfileViewModel::class.java
        )
        handleObserver()

    }

    private fun handleObserver() {
        mProfileEditViewModel.getprofileUnAuthorized().observe(viewLifecycleOwner, Observer {
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


        mProfileEditViewModel.getEditProfileObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            Log.d("TAG", "UserId:" + it.UserId)
            mProfileViewModel.getProfile(
                mSessionManager.getAuthorization(),
                it.UserId
            )


        })

        mProfileViewModel.getProfileObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            mSessionManager.setProfileModel(it)
            mProfileModel = mSessionManager.getProfileModel()
            val alertModel = AlertModel(
                2000, resources.getString(R.string.profile_update), resources.getString(
                    R.string.profile_update_successfully
                ), R.drawable.correct_icon, R.color.notiSuccessColor
            )
            mCommonUtils.showAlert(
                alertModel.duration,
                alertModel.title,
                alertModel.message,
                alertModel.drawable,
                alertModel.color,
                requireActivity()

            )

            setUIValues()
        })

        mProfileViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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


        mProfileViewModel.getProgressState()?.observe(viewLifecycleOwner,
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

        mProfileEditViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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


        mProfileEditViewModel.getProgressState()?.observe(viewLifecycleOwner,
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


    private fun handleClickListner() {
        mBinding.submitButton.setOnClickListener {
            if ( !CommonUtils.isValidEmail(mEmailLayoutBinding.tvUserEmail.text.toString())) {
                val alertModel = AlertModel(
                    2000, resources.getString(R.string.profile_update), resources.getString(
                        R.string.Please_Enter_Valid_Email_Id
                    ), R.drawable.wrong_icon, R.color.notiFailColor
                )

                mCommonUtils.showAlert(
                    alertModel.duration,
                    alertModel.title,
                    alertModel.message,
                    alertModel.drawable,
                    alertModel.color,
                    requireActivity()

                )
            }
            else {
                var mProfileRequestModel: ProfileEditRequestModel? = ProfileEditRequestModel(
                    mSessionManager.getUserDetailLoginModel()?.memberID,
                    mPhoneLayoutBinding.tvUserPhone.text.toString(),
                    mEmailLayoutBinding.tvUserEmail.text.toString(),
                    mCommonUtils.date_dd_MM_yyyy(
                        mDOBLayoutBinding.tvUserDob.text.toString()
                    ),
                    mAddressLayoutBinding.tvUserAdress.text.toString(),
                )
                mProfileEditViewModel.editProfile(
                    mSessionManager.getAuthorization(),
                    mProfileRequestModel
                )
            }
        }
    }


    private fun setUIValues() {

        if(mProfileModel?.dateOfBirth != null)
        mDOBLayoutBinding.tvUserDob.setText(mCommonUtils.date_dd_MMM_yyyy(mProfileModel?.dateOfBirth))
        mPhoneLayoutBinding.tvUserPhone.setText(mProfileModel?.phoneNumber)
        mEmailLayoutBinding.tvUserEmail.setText(mProfileModel?.email)
        mAddressLayoutBinding.tvUserAdress.setText(mProfileModel?.address)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileEditFragment().apply {

            }
    }
}