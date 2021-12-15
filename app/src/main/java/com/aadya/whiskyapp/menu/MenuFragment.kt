package com.aadya.whiskyapp.menu

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentMenuBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.databinding.SecretcodeNewLayoutBinding
import com.aadya.whiskyapp.events.viewmodel.EventsFactory
import com.aadya.whiskyapp.events.viewmodel.EventsViewModel
import com.aadya.whiskyapp.menu.viewmodel.MenuFactory
import com.aadya.whiskyapp.menu.viewmodel.MenuViewModel
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.profile.viewmodel.ProfileViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager


class MenuFragment : Fragment() {



    private lateinit var mBinding: FragmentMenuBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mSessionManager: SessionManager
    private lateinit var mMenuViewModel : MenuViewModel
    private lateinit var mCommonUtils : CommonUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        handleObserver()
        return mBinding.root
    }

    private fun handleObserver() {
        mMenuViewModel.getEventsObserver().observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "In Observer event")
            if (it == null) return@Observer

            mBinding.pdfView.webViewClient = WebViewClient()
            mBinding.pdfView.settings.setSupportZoom(true)
            mBinding.pdfView.settings.javaScriptEnabled = true
            val url = it.imageName
            mBinding.pdfView.loadUrl(CommonUtils.APIURL.Event_IMAGE_URL+url)
        })

        mMenuViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {

                    Log.d("TAG", "In Observer alert")
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


        mMenuViewModel.getProgressState()?.observe(viewLifecycleOwner,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    Log.d("TAG", "In Observer progress")
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
        mSessionManager = SessionManager.getInstance(requireContext())!!
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu,
            container,
            false
        )

        setIncludedLayout()

        mMenuViewModel = ViewModelProvider(this, MenuFactory(activity?.application)).get(
            MenuViewModel::class.java
        )
        mMenuViewModel.getMenu(mSessionManager?.getAuthorization())

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            MenuFragment().apply {
            }
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

    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

}