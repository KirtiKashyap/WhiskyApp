package com.aadya.whiskyapp.scanlog.ui

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
import com.aadya.whiskyapp.databinding.FragmentScanLogBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.scanlog.model.ScanLogRequest
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager


class ScanLogFragment : Fragment() {
    private lateinit  var mScanLogAdapter: ScanLogAdapter
    private lateinit var mSessionManager: SessionManager
    private lateinit var mScanLogViewModel : ScanLogViewModel
    private lateinit var mCommonUtils : CommonUtils
    private lateinit var mBinding: FragmentScanLogBinding
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
        setUpRecyclerView(mBinding.scanLogRecyclerView)
        handleObserver()
        return mBinding.root
    }
    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        mScanLogAdapter = ScanLogAdapter(
            requireContext()
        )
        recyclerView.adapter = mScanLogAdapter
    }
    private fun handleObserver() {
        mScanLogViewModel.getScanLogObserver().observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "In Observer event")
            if (it?.isEmpty() == true) {
                mBinding.emptyTv.visibility = View.VISIBLE
                mBinding.scanLogRecyclerView.visibility = View.GONE
            } else {
                mBinding.emptyTv.visibility = View.GONE
                mBinding.scanLogRecyclerView.visibility = View.VISIBLE
                if (it != null) {
                    mScanLogAdapter.notifyData(it)
                }
            }
        })

        mScanLogViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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


        mScanLogViewModel.getProgressState()?.observe(viewLifecycleOwner,
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

    private fun initializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(requireContext())!!
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_scan_log,
            container,
            false
        )
        setIncludedLayout()

        mScanLogViewModel = ViewModelProvider(this, ScanLogFactory(activity?.application)).get(
            ScanLogViewModel::class.java
        )
        var scanLogRequest=ScanLogRequest()
        scanLogRequest.MemberID=mSessionManager.getProfileModel()!!.memberID
        mScanLogViewModel.getScanLog(mSessionManager?.getAuthorization(),scanLogRequest)
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
            ScanLogFragment().apply {
            }
    }

}