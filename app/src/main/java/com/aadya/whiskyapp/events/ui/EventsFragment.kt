package com.aadya.whiskyapp.events.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.aadya.whiskyapp.MyApplication
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.*
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.events.model.RSVPRequestModel
import com.aadya.whiskyapp.events.viewmodel.RSVPFactory
import com.aadya.whiskyapp.events.viewmodel.RSVPViewModel
import com.aadya.whiskyapp.payment.ui.CheckoutActivityJava
import com.aadya.whiskyapp.payment.ui.PayActivity
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.utils.*
import com.aadya.whiskyapp.utils.CommonUtils.APIURL.Companion.Event_IMAGE_URL
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.customview_layout.*
import kotlinx.android.synthetic.main.events_header.view.*
import kotlinx.android.synthetic.main.fragment_event_new.*


private const val ARG_EVENTMODEL = "eventModel"
private const val CHECK = "check"
private const val ARG_POSITION = "position"
private const val FROM_DIALOG = "FROM_DIALOG"

class EventsFragment() : Fragment(),AdapterView.OnItemSelectedListener{
    private lateinit var mBinding: EventFrgmentMyBinding
//    private lateinit var mBinding: FragmentEventNewDailogBinding
    private lateinit var mIncludedLayoutBinding: EventsHeaderBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var eventModel: EventsResponseModel
    private var pos : Int = 0
    /* set from spinner */
    private var isFromDialog=false
    private var check=""
    var onEventsPageSwipeUpListner: onEventsPageSwipeUpListner? = null
    private var mdeletePageViewPager : deletePageViewPager? = null
    private lateinit var mSessionManager: SessionManager
    private lateinit var mRSVPViewModel : RSVPViewModel
    private lateinit var mCommonUtils : CommonUtils
    private lateinit var noOfGuestPass: ArrayList<Int>
    private lateinit var noOfGuestAdapter: ArrayAdapter<Int>
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
            isFromDialog=it.getBoolean(FROM_DIALOG)
            check= it.getString(CHECK).toString()

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

        mBinding.plusOne.setOnCheckedChangeListener { buttonView, isChecked ->
            MyApplication.isPlusOne=isChecked
        }
        mBinding.tvBuy.setOnClickListener {
            activity?.let{



                if(mSessionManager.getProfileModel()!!.paymentMethodID) {

                    val intent = Intent(it, PayActivity::class.java)
                    intent.putExtra("amount", eventModel.price)
                    intent.putExtra("itemType", "E")
                    intent.putExtra("itemId", eventModel.eventID!!)
                    intent.putExtra("memberId", mSessionManager.getUserDetailLoginModel()?.memberID)
                    intent.putExtra("authorization", mSessionManager.getAuthorization())
                    intent.putExtra("imageName", eventModel.imageName!!)
                    intent.putExtra("eventTitle", eventModel.eventTitle!!)
                    it.startActivity(intent)


                }else{
                    val intent = Intent(it, CheckoutActivityJava::class.java)
                    intent.putExtra("amount", eventModel.price)
                    intent.putExtra("itemType", "E")
                    intent.putExtra("itemId", eventModel.eventID!!)
                    intent.putExtra("memberId", mSessionManager.getUserDetailLoginModel()?.memberID)
                    intent.putExtra("authorization", mSessionManager.getAuthorization())
                    intent.putExtra("email", mSessionManager.getProfileModel()!!.email)
                    intent.putExtra("description",mSessionManager.getProfileModel()!!.description)
                    intent.putExtra("address",mSessionManager.getProfileModel()!!.address)
                    intent.putExtra("name",mSessionManager.getProfileModel()!!.firstName)
                    intent.putExtra("auth",mSessionManager.getAuthorization())
                    it.startActivity(intent)
                }


                if(isFromDialog){
                    //TODO dismiss event dailogue
                }
            }
        }
        return mBinding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUi() {
        if(eventModel!=null){
            if(eventModel.eventTypeName.equals("Purchase")){
                mBinding.tvBuy.visibility=View.VISIBLE
                mBinding.priceTextView.visibility=View.VISIBLE
                mBinding.priceTextView.text=eventModel.price
                mBinding.tvRsvp.visibility=View.GONE
                mBinding.imgRsvpNotintersted.visibility=View.GONE
                mBinding.rsvpLayout.visibility=View.GONE
                mBinding.imgRsvpIntersted.visibility=View.GONE
                mBinding.imgRsvp.visibility=View.GONE

            }else{
                mBinding.rsvpLayout.visibility=View.VISIBLE
                mBinding.imgRsvpNotintersted.visibility=View.VISIBLE
                mBinding.imgRsvpIntersted.visibility=View.VISIBLE
                mBinding.tvRsvp.visibility=View.VISIBLE
                mBinding.imgRsvp.visibility=View.VISIBLE
                mBinding.tvBuy.visibility=View.GONE
                mBinding.priceTextView.visibility=View.GONE

            }
            if(mSessionManager.getProfileModel()!!.plusOne==null){
                mBinding.plusOne.visibility=View.GONE
                mBinding.passtextView.visibility=View.GONE
            }else{
                mBinding.plusOne.visibility=View.VISIBLE
                mBinding.passtextView.visibility=View.VISIBLE
            }

            val list: List<String>? = eventModel.eventTitle?.trim()?.split("\\s+".toRegex())
            if(list?.size!! >= 3 )
            {
                mBinding.tvEventname1.text = list[0]
                mBinding.tvEventname2.text = list[1]

                for (i in list.subList(2,list.size)) {
                    print(i)
                    mBinding.tvEventname3.append("$i ")
                }

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
                        .load(Event_IMAGE_URL + eventModel.imageName)
                        .into(mBinding.imgEventtop)
                }

            }

            mBinding.tvDay.text = mCommonUtils.getWeek_Day(
                mCommonUtils.convertString_To_Date(
                    eventModel.eventDate.toString()
                )
            )
            mBinding.tvMonth.text = mCommonUtils.getMonth_From_Date(
                mCommonUtils.convertString_To_Date(
                    eventModel.eventDate.toString()
                )
            )

            if(eventModel.eventStartTime?.isNotEmpty() == true){}
            mBinding.tvStarttime.text = eventModel.eventStartTime
            if(eventModel.eventEndTime?.isNotEmpty() == true){}
            mBinding.tvEndtime.text = eventModel.eventEndTime
            mBinding.tvDate.text = mCommonUtils.getDay_From_Date(
                mCommonUtils.convertString_To_Date(
                    eventModel.eventDate.toString()
                )
            )


                if(check == "FromAttendee"){
                    mBinding.spinnerGuestPass.visibility=View.GONE
                    if(eventModel.availGuestPasses>0){
                        mBinding.passtextView.visibility=View.VISIBLE
                        mBinding.passtextView.text="Guest Pass: "+eventModel.availGuestPasses
                    }else{
                        mBinding.passtextView.visibility=View.GONE
                    }

                }else {
                    try {
                        if (eventModel.availGuestPasses > 0) {
                            mBinding.spinnerGuestPass.visibility = View.VISIBLE
                            mBinding.passtextView.text = "Avail Guest Pass: "
                            noOfGuestPass = ArrayList<Int>()
                            val arrayName = Array(
                                (eventModel.availGuestPasses),
                                { i -> i * 1 })
                            noOfGuestPass.add(0)
                            for (i in 0..arrayName.size - 1) {
                                println(arrayName[i])
                                noOfGuestPass.add(i + 1)
                            }

                            noOfGuestAdapter = ArrayAdapter<Int>(
                                requireContext(),
                                R.layout.row_spinner,
                                noOfGuestPass
                            )
                            noOfGuestAdapter.setDropDownViewResource(R.layout.row_spinner_dialog)
                            mBinding.spinnerGuestPass.adapter = noOfGuestAdapter
                            mBinding.spinnerGuestPass.onItemSelectedListener = this
                        } else {
                            mBinding.spinnerGuestPass.visibility = View.GONE
                            mBinding.passtextView.visibility = View.GONE
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
            }
        }
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.event_frgment_my,
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
            var msg1: String = ""
            var msg2: String = ""
            if (it.EventFeedbackID.equals("1")) {

                msg1 = "Thanks for your RSVP for the Event<b> ${eventModel.eventTitle} </b>."
                msg2 = "\n" +
                        " \n" +
                        "  We have marked you up for attending the <b> ${eventModel.eventTitle}</b>."
                launchFragment(
                    RSVPAcknowledgeFragment.newInstance(msg1, msg2),
                    "RSVPAcknowledgeFragment"
                )
            } else if (it.EventFeedbackID.equals("2")) {
                msg1 = "You have been opted out yourself for the RSVP for the event<b> ${eventModel.eventTitle} </b>."
                msg2 = "\n" +
                        " \n" +
                        "  We have unmarked you for the same."
                /* <b> ${eventModel.eventTitle}</b>.*/
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
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
        }
        if(isFromDialog){
            mIncludedLayoutBinding.imgDrawer.visibility=View.GONE
            mIncludedLayoutBinding.root.img_logo.visibility=View.GONE
        }else{
            mIncludedLayoutBinding.imgDrawer.visibility=View.VISIBLE
            mIncludedLayoutBinding.root.img_logo.visibility=View.VISIBLE
        }
        mIncludedLayoutBinding.root.visibility=View.VISIBLE
        mIncludedLayoutBinding.imgLogo.setOnClickListener {
            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }

        mBinding.imgRsvpIntersted.setOnClickListener{
            mBinding.imgRsvpIntersted.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.pulse
                )
            )
            mBinding.imgRsvpIntersted.isClickable=false

            Handler(Looper.getMainLooper()).postDelayed({
                var mRSVPRequestModel = RSVPRequestModel()
                mRSVPRequestModel.EventID = eventModel.eventID
                mRSVPRequestModel.EventFeedbackID = 1
                mRSVPRequestModel.remainingGuestPasses=   MyApplication.mSelectedGuestPass
                mRSVPRequestModel.PlusOne=  MyApplication.isPlusOne
                mRSVPViewModel.getRSVP(mSessionManager.getAuthorization(), mRSVPRequestModel)
                mBinding.imgRsvpIntersted.isClickable = true
                mBinding.imgRsvpIntersted.clearAnimation()

            }, 2000)
        }

        mBinding.imgRsvpNotintersted.setOnClickListener{
            mBinding.imgRsvpNotintersted.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.pulse
                )
            )
            mBinding.imgRsvpNotintersted.isClickable=false
            Handler(Looper.getMainLooper()).postDelayed({
                var mRSVPRequestModel = RSVPRequestModel()
                mRSVPRequestModel.EventID = eventModel.eventID
                mRSVPRequestModel.EventFeedbackID = 2
                mRSVPRequestModel.remainingGuestPasses=  MyApplication.mSelectedGuestPass
                mRSVPRequestModel.PlusOne=  MyApplication.isPlusOne
                mRSVPViewModel.getRSVP(mSessionManager.getAuthorization(), mRSVPRequestModel)
                mBinding.imgRsvpNotintersted.isClickable = true
                mBinding.imgRsvpNotintersted.clearAnimation()
            }, 2000)
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
        fun newInstance(
            eventsModel: EventsResponseModel,
            position: Int,
            deletePageViewPager: deletePageViewPager?,
            isFromDialog: Boolean
        ) =

        EventsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_EVENTMODEL, eventsModel)
                putInt(ARG_POSITION, position)
                putBoolean(FROM_DIALOG, isFromDialog)
            }
            this.mdeletePageViewPager = deletePageViewPager
        }

        fun newInstance1(event: EventsResponseModel?,check: String) =

            EventsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENTMODEL, event)
                    putString(CHECK, check)
                }
            }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       // if(position != 0)
            if(noOfGuestPass[position]<=eventModel.remainingGuestPasses) {
                MyApplication.mSelectedGuestPass = noOfGuestPass[position]
            }else{
                Toast.makeText(requireContext(),"Not More Then Remaining guest pass",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        MyApplication.mSelectedGuestPass = 0
    }
}