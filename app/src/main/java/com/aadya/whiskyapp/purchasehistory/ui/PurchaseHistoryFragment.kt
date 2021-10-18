package com.aadya.whiskyapp.purchasehistory.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentPurchaseHistoryBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.events.adapter.EventAttendingAdapter
import com.aadya.whiskyapp.events.viewmodel.EventAttendingFactory
import com.aadya.whiskyapp.events.viewmodel.EventAttendingViewModel
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.purchasehistory.adapter.PurchaseHistoryAdapter
import com.aadya.whiskyapp.purchasehistory.viewmodel.PurchaseHistoryFactory
import com.aadya.whiskyapp.purchasehistory.viewmodel.PurchaseHistoryViewModel
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.specialoffers.ui.SpecialofferDetailFragment
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager


class PurchaseHistoryFragment : Fragment() {
    private lateinit var mBinding: FragmentPurchaseHistoryBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mSessionManager: SessionManager
    private lateinit var mCommonUtils : CommonUtils
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit  var mPurchaseHistoryAdapter: PurchaseHistoryAdapter
    private lateinit var mPurchaseHistoryViewModel: PurchaseHistoryViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        setUpRecyclerView(mBinding.purchaseHistoryRecyclerView)
        handleObserver()
        return mBinding.root
    }

    private fun handleObserver() {
        mPurchaseHistoryViewModel.getPurchaseHistoryListObserver().observe(viewLifecycleOwner, Observer {
            if (it?.isEmpty() == true) {
                mBinding.emptyTv.visibility = View.VISIBLE
                mBinding.purchaseHistoryRecyclerView.visibility = View.GONE
            } else {
                mBinding.emptyTv.visibility = View.GONE
                mBinding.purchaseHistoryRecyclerView.visibility = View.VISIBLE
                if (it != null) {
                    mPurchaseHistoryAdapter.notifyData(it)
                }
            }

        })

        mPurchaseHistoryViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {

                    Log.d("TAG","In Observer alert")
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


        mPurchaseHistoryViewModel.getProgressState()?.observe(viewLifecycleOwner,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    Log.d("TAG","In Observer progress")
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), requireContext()
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })

        mPurchaseHistoryViewModel.getPurchaseHistoryUnAuthorized().observe(viewLifecycleOwner, Observer {
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

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        mPurchaseHistoryAdapter = PurchaseHistoryAdapter(
            requireContext(),

            object : PurchaseHistoryAdapter.PurchaseHistoryListItemClick {
                override fun onItemClick(
                   mSpecialOfferResponseModel: SpecialOfferResponseModel,

                ) {
                    val ft = activity?.supportFragmentManager?.beginTransaction()
                    ft?.replace(
                        R.id.app_container,
                        SpecialofferDetailFragment.newInstance(mSpecialOfferResponseModel),
                        "SpecialofferDetailFragment"
                    )
                    ft?.addToBackStack(null)
                    ft?.commit()

                }
            }

        )
        recyclerView.adapter = mPurchaseHistoryAdapter
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_purchase_history,
            container,
            false
        )
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(context)!!
        setIncludedLayout()

        mPurchaseHistoryViewModel = ViewModelProvider(this, PurchaseHistoryFactory(activity?.application)).get(
            PurchaseHistoryViewModel::class.java
        )

        mPurchaseHistoryViewModel.getPurchaseHistoryList(mSessionManager?.getAuthorization())
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
            PurchaseHistoryFragment().apply {
            }
    }
}