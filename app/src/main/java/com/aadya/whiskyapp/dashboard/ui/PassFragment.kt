package com.aadya.whiskyapp.dashboard.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentPassBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager


class PassFragment : Fragment() {
    private lateinit var mBinding: FragmentPassBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(requireContext())!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)

        // Inflate the layout for this fragment
        mIncludedLayoutBinding.imgLogo.setOnClickListener {
            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }
        return mBinding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pass,
            container,
            false
        )
        setIncludedLayout()
        setData()
    }

    private fun setData() {
        mBinding.nogustTextView.text=mSessionManager.getProfileModel()?.numberOfGuestPasses.toString()
        mBinding.regustTextView.text=mSessionManager.getProfileModel()?.remainingGuestPasses.toString()
    }

    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
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
            PassFragment().apply {
            }

    }
}