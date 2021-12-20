package com.aadya.whiskyapp.reserve.ui

import android.content.Context
import android.os.Bundle
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
import com.aadya.whiskyapp.databinding.FragmentReservationHistoryBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.reserve.model.ReserveInfoRequest
import com.aadya.whiskyapp.reserve.viewmodel.ReserveFactory
import com.aadya.whiskyapp.reserve.viewmodel.ReserveViewModel
import com.aadya.whiskyapp.scanlog.ui.ScanLogFragment
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager

class ReservationHistoryFragment : Fragment() {
    private lateinit  var mReservationHistoryAdapter: ReservationHistoryAdapter
    private lateinit var mSessionManager: SessionManager
    private lateinit var mReserveViewModel : ReserveViewModel
    private lateinit var mCommonUtils : CommonUtils
    private lateinit var mBinding: FragmentReservationHistoryBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private var mDrawerInterface: DrawerInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initializeMembers(inflater, container)
        setUpRecyclerView(mBinding.reserveHistoryLogRecyclerView)
        handleObserver()
        return mBinding.root
    }

    private fun handleObserver() {
        mReserveViewModel.getReserveHistoryLogObserver().observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "In Observer event")
            if (it?.isEmpty() == true) {
                mBinding.emptyTv.visibility = View.VISIBLE
                mBinding.reserveHistoryLogRecyclerView.visibility = View.GONE
            } else {
                mBinding.emptyTv.visibility = View.GONE
                mBinding.reserveHistoryLogRecyclerView.visibility = View.VISIBLE
                if (it != null) {
                    mReservationHistoryAdapter.notifyData(it)
                }
            }
        })

        mReserveViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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


        mReserveViewModel.getProgressState()?.observe(viewLifecycleOwner,
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


    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        mReservationHistoryAdapter = ReservationHistoryAdapter(
            requireContext()
        )
        recyclerView.adapter = mReservationHistoryAdapter
    }

    private fun initializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(requireContext())!!
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reservation_history,
            container,
            false
        )
        setIncludedLayout()

        mReserveViewModel = ViewModelProvider(this, ReserveFactory(activity?.application)).get(
            ReserveViewModel::class.java
        )
        var reserveLogRequest= ReserveInfoRequest()
        reserveLogRequest.memberID=mSessionManager.getProfileModel()!!.memberID
        mReserveViewModel.getReserveHistoryLog(mSessionManager?.getAuthorization(),reserveLogRequest)
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
            ReservationHistoryFragment().apply {
            }
    }

}