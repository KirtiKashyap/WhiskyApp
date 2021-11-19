package com.aadya.whiskyapp.specialoffers.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentSpecialOfferViewPagerBinding
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.menu.MenuFragment
import com.aadya.whiskyapp.specialoffers.adapter.SpecialOfferAdapter
import com.aadya.whiskyapp.specialoffers.viewmodel.SpecialOfferFactory
import com.aadya.whiskyapp.specialoffers.viewmodel.SpecialOfferViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.BottomNavigationInterface
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.CommonUtils.specialoffer_adapter_position
import com.aadya.whiskyapp.utils.SessionManager


class SpecialOfferViewPagerFragment : Fragment() {



    private lateinit var mBinding: FragmentSpecialOfferViewPagerBinding
    private lateinit var specialOfferAdapter: SpecialOfferAdapter
    private lateinit var mViewModel: SpecialOfferViewModel
    private lateinit var mCommonUtils: CommonUtils
    private  var mSessionManager: SessionManager? = null
    private var mBottomNavigationInterface: BottomNavigationInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mBottomNavigationInterface = context as BottomNavigationInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        return  return mBinding.root
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mCommonUtils = CommonUtils

        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_special_offer_view_pager,
            container,
            false
        )

        mSessionManager = SessionManager.getInstance(requireContext())
        mViewModel = ViewModelProvider(this, SpecialOfferFactory(activity?.application)).get(
            SpecialOfferViewModel::class.java
        )
        mViewModel.getSpecialOffer(mSessionManager?.getAuthorization())

        handleObserver()


    }


    private fun handleObserver() {
        mViewModel.getSpecialOfferObserver().observe(viewLifecycleOwner, Observer {

            if(it.isNullOrEmpty())
            {
                val alertModel = AlertModel(
                    2000, resources.getString(R.string.special_error),
                    resources.getString(R.string.no_special_offers)
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
                    mBottomNavigationInterface?.setOnBottomNavigationResult()
                    launchFragment(MenuFragment.newInstance(), "MenuFragment")}, 2000)

            }

            else {
                specialOfferAdapter = SpecialOfferAdapter(
                    childFragmentManager, it,false
                )
                mBinding.specialofferViewpager.adapter = specialOfferAdapter
            }




        })

        mViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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


        mViewModel.getProgressState()?.observe(viewLifecycleOwner,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), requireActivity()
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })

        mViewModel.getprofileUnAuthorized().observe(viewLifecycleOwner, Observer {
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
            SpecialOfferViewPagerFragment().apply {

            }
    }
}