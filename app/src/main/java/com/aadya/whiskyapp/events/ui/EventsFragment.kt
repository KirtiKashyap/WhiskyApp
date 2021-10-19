package com.aadya.whiskyapp.events.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.EventDateTimeBinding
import com.aadya.whiskyapp.databinding.EventRsvpBinding
import com.aadya.whiskyapp.databinding.EventsHeaderBinding
import com.aadya.whiskyapp.databinding.FragmentEventNewBinding
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.events.model.RSVPRequestModel
import com.aadya.whiskyapp.events.viewmodel.RSVPFactory
import com.aadya.whiskyapp.events.viewmodel.RSVPViewModel
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.utils.*
import com.bumptech.glide.Glide


private const val ARG_EVENTMODEL = "eventModel"
private const val ARG_POSITION = "position"
private const val IMAGE_URL="http://92.204.128.4:5300/UploadFiles/EventImage/"

class EventsFragment() : Fragment() {

    private lateinit var mBinding: FragmentEventNewBinding
    private lateinit var mIncludedLayoutBinding: EventsHeaderBinding
    private  lateinit var mDateTimeIncludedLayout : EventDateTimeBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var eventModel: EventsResponseModel
    private var pos : Int = 0
    var onEventsPageSwipeUpListner: onEventsPageSwipeUpListner? = null
    private var mdeletePageViewPager : deletePageViewPager? = null
    private lateinit var mIncludedRSVPBinding: EventRsvpBinding
    private lateinit var mSessionManager: SessionManager
    private lateinit var mRSVPViewModel : RSVPViewModel
    private lateinit var mCommonUtils : CommonUtils

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)

        arguments?.let {
            eventModel = it.getParcelable(ARG_EVENTMODEL)!!
            pos = it.getInt(ARG_POSITION)
        }

        handleObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        onEventsPageSwipeUpListner?.getLiveData()?.observe(viewLifecycleOwner, Observer {

            val ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.replace(
                R.id.app_container,
                EventDetailFragment.newInstance(eventModel),
                "EventDetailFragment"
            )
            ft?.addToBackStack(null)
            ft?.commit()

        })
        setUi()
        return mBinding.root
    }

    private fun setUi() {
        if(eventModel!=null){

            val list: List<String>? = eventModel.eventTitle?.trim()?.split("\\s+".toRegex())
            if(list?.size!! >= 3 )
            {
                mBinding.tvEventname1.text = list[0]
                mBinding.tvEventname2.text = list[1]
                mBinding.tvEventname3.text = list[2]

            }
            else if(list?.size == 2){
                mBinding.tvEventname1.text = list[0]
                mBinding.tvEventname2.text = list[1]
            }
            else if(list?.size == 1){
                mBinding.tvEventname1.text = list[0]
            }

            if(eventModel.imageName?.isNullOrEmpty()==false){
                context?.let {
                    Glide.with(it)
                        .load(IMAGE_URL+eventModel.imageName)
                        .into(mBinding.imgEventtop)
                }

            }
            mDateTimeIncludedLayout.tvDay.text = mCommonUtils.getWeek_Day(mCommonUtils.convertString_To_Date(eventModel.eventDate.toString()))
            mDateTimeIncludedLayout.tvMonth.text = mCommonUtils.getMonth_From_Date(mCommonUtils.convertString_To_Date(eventModel.eventDate.toString()))

            if(eventModel.eventStartTime?.isNotEmpty() == true)
            mDateTimeIncludedLayout.tvStarttime.text = eventModel.eventStartTime
            if(eventModel.eventEndTime?.isNotEmpty() == true)
            mDateTimeIncludedLayout.tvEndtime.text = eventModel.eventEndTime
            mDateTimeIncludedLayout.tvDate.text = mCommonUtils.getDay_From_Date(mCommonUtils.convertString_To_Date(eventModel.eventDate.toString()))



        }
    }


    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {


        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_event_new,
            container,
            false
        )

        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(context)!!


        setIncludedLayout()

        handleclickListner()





    }



    private fun handleclickListner() {

        onEventsPageSwipeUpListner = onEventsPageSwipeUpListner(
            requireContext(),
            mBinding.constraintEvents
        )

    }


    private fun handleObserver() {
        mRSVPViewModel = ViewModelProvider(this, RSVPFactory(activity?.application)).get(
            RSVPViewModel::class.java
        )

        mRSVPViewModel.getRSVPObserver().observe(this, Observer {
            if (it == null) return@Observer
            var msg1 : String = ""
            var msg2 : String = ""
            if(it.EventFeedbackID.equals("1")) {
                 msg1 = "Thanks for your RSVP for the Event<b> ${eventModel.eventTitle} </b>."
                 msg2 = "\n" +
                        " \n" +
                        "  We have marked you up for attending the <b> ${eventModel.eventTitle}</b>."
                launchFragment(
                    RSVPAcknowledgeFragment.newInstance(msg1, msg2),
                    "RSVPAcknowledgeFragment"
                )
            }
            else if(it.EventFeedbackID.equals("2")) {
                msg1 = "Thanks for your RSVP for the Event<b> ${eventModel.eventTitle} </b>."
                msg2 = "\n" +
                        " \n" +
                        "  We have marked you down for attending the <b> ${eventModel.eventTitle}</b>."
                launchFragment(
                    RSVPAcknowledgeFragment.newInstance(msg1, msg2),
                    "RSVPAcknowledgeFragment"
                )
            }



        })

        mRSVPViewModel.getAlertViewState()?.observe(this,
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


        mRSVPViewModel.getProgressState()?.observe(this,
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


    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.eventHeader
        mIncludedLayoutBinding.imgClose.setOnClickListener {
            mdeletePageViewPager?.updateView(pos)
        }
        mIncludedLayoutBinding.imgLogo.setOnClickListener {
            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }
        mIncludedRSVPBinding = mBinding.eventRsvp
        mIncludedRSVPBinding.imgRsvpIntersted.setOnClickListener{

            var mRSVPRequestModel : RSVPRequestModel= RSVPRequestModel()
            mRSVPRequestModel.EventID = eventModel.eventID
            mRSVPRequestModel.EventFeedbackID = 1
            mRSVPViewModel.getRSVP(mSessionManager.getAuthorization(),mRSVPRequestModel)



        }

        mIncludedRSVPBinding.imgRsvpNotintersted.setOnClickListener{
            var mRSVPRequestModel : RSVPRequestModel= RSVPRequestModel()
            mRSVPRequestModel.EventID = eventModel.eventID
            mRSVPRequestModel.EventFeedbackID = 2
            mRSVPViewModel.getRSVP(mSessionManager.getAuthorization(),mRSVPRequestModel)
        }

        mDateTimeIncludedLayout = mBinding.eventDateTime
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            eventsModel: EventsResponseModel,

            position: Int,
            deletePageViewPager: deletePageViewPager?
        ) =

        EventsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_EVENTMODEL, eventsModel)
                putInt(ARG_POSITION, position)
            }
            this.mdeletePageViewPager = deletePageViewPager
        }

    }





}