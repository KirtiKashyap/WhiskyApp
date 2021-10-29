package com.aadya.whiskyapp.events.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentLauncheventsBinding
import com.aadya.whiskyapp.events.adapter.EventsAdapter
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.events.viewmodel.EventsFactory
import com.aadya.whiskyapp.events.viewmodel.EventsViewModel
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.menu.MenuFragment
import com.aadya.whiskyapp.profile.ui.SecretCodeFragment
import com.aadya.whiskyapp.utils.*
import kotlin.collections.ArrayList


class EventsLaunchFragment : Fragment() , deletePageViewPager ,updateEventsViewPager{

    private lateinit var mBinding: FragmentLauncheventsBinding
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var mEventsViewModel : EventsViewModel
    private var eventsList = ArrayList<EventsResponseModel>()
    private lateinit var  mCommonUtils : CommonUtils
    private  var mSessionManager: SessionManager? = null
    private var mBottomNavigationInterface: BottomNavigationInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mBottomNavigationInterface = context as BottomNavigationInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        handleObserver()

        return mBinding.root
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

    private fun handleObserver() {
        mEventsViewModel.getEventsObserver().observe(viewLifecycleOwner, Observer {
            Log.d("TAG","In Observer event")
            if(it.isNullOrEmpty())
                {
                    val alertModel = AlertModel(
                        2000, resources.getString(R.string.event_error),
                            resources.getString(R.string.sorry_empty_events)
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

                    Handler(Looper.getMainLooper()).postDelayed({
                        mBottomNavigationInterface?.setOnBottomNavigationResult()
                        launchFragment(MenuFragment.newInstance(), "MenuFragment")}, 1500)

                }

            else {
                eventsList.clear()
                eventsList.addAll(it)
                eventsAdapter.setItems(it)
            }

        })

        mEventsViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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


        mEventsViewModel.getProgressState()?.observe(viewLifecycleOwner,
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

        mEventsViewModel.getprofileUnAuthorized().observe(viewLifecycleOwner, Observer {
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


    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(requireContext())
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_launchevents,
            container,
            false
        )


        eventsList = ArrayList<EventsResponseModel>()

        eventsAdapter = EventsAdapter(
            childFragmentManager,this,eventsList,this
        )
        mBinding.eventsViewpager.adapter = eventsAdapter

        mEventsViewModel = ViewModelProvider(this, EventsFactory(activity?.application)).get(
            EventsViewModel::class.java
        )

        mEventsViewModel.getEvents(mSessionManager?.getAuthorization())
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            EventsLaunchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    override fun updateView(position: Int) {
        eventsAdapter.notifyData(position)
    }

    override fun updateEventsViewPager() {
        mBottomNavigationInterface?.setOnBottomNavigationResult()
        launchFragment(
            MenuFragment.newInstance(),
            "MenuFragment"
        )
    }
}