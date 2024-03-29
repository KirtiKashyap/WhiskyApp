package com.aadya.whiskyapp.events.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
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
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.bumptech.glide.Glide


private const val ARG_EVENT = "events"
class EventDetailFragment : Fragment() {
    var mRemainingGuestPasses:Int=0
    private var eventModel: EventsResponseModel? = null
    private lateinit var mBinding: EventDetailBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mIncludedRSVPDetailBinding: EventDetailRsvpBinding
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
        enterTransition = inflater.inflateTransition(R.transition.fade)
        arguments?.let {
            eventModel = it.getParcelable(ARG_EVENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        handleObserver()
        setUI()
        return mBinding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUI() {
        if(eventModel != null){
            mBinding.tvEventname.text = eventModel?.eventTitle

            if(eventModel!!.eventTypeName.equals("Purchase")){
                mBinding.priceTextView.text=eventModel!!.price
                mBinding.buyLayout.visibility=View.VISIBLE
                mIncludedRSVPDetailBinding.rsvpLayout.visibility=View.GONE

            }else{
                mBinding.buyLayout.visibility=View.GONE
                mIncludedRSVPDetailBinding.rsvpLayout.visibility=View.VISIBLE
            }

            var when_str : String = mCommonUtils.getWeek_Day(mCommonUtils.convertString_To_Date(eventModel?.eventDate.toString())) + ",  " +
                    mCommonUtils.convertString_To_Date_dd_MMM_yyyy_format(eventModel?.eventDate.toString())
            if(eventModel?.eventStartTime != null)
                 when_str.plus(eventModel?.eventStartTime)

            mBinding.tvWhen.text = when_str

            mBinding.tvWhere.text = eventModel?.eventLocation
            mBinding.tvWhatIsSpecial.text = eventModel?.description

            if(eventModel?.imageName?.isNullOrEmpty()==false){
                context?.let {
                    Glide.with(it)
                        .load(CommonUtils.APIURL.Event_IMAGE_URL +eventModel?.imageName)
                        .into(mBinding.imgEventtop)
                }

            }

        }
    }

    private fun handleObserver() {

        mRSVPViewModel.getRSVPObserver().observe(viewLifecycleOwner, Observer {
            /*if (it == null) return@Observer
            var msg1 ="Thanks for your RSVP for the Event<b> ${eventModel?.eventTitle} </b>."
            var msg2 = "\n" +
                    " \n" +
                    "  We have marked you up for attending the <b> ${eventModel?.eventTitle}</b>."
            launchFragment(RSVPAcknowledgeFragment.newInstance(msg1, msg2), "RSVPAcknowledgeFragment")*/

            if (it == null) return@Observer
            var msg1: String = ""
            var msg2: String = ""
            if (it.EventFeedbackID.equals("1")) {
                msg1 = "Thanks for your RSVP for the Event<b> ${eventModel?.eventTitle} </b>."
                msg2 = "\n" +
                        " \n" +
                        "  We have marked you up for attending the <b> ${eventModel?.eventTitle}</b>."
                launchFragment(
                    RSVPAcknowledgeFragment.newInstance(msg1, msg2),
                    "RSVPAcknowledgeFragment"
                )
            } else if (it.EventFeedbackID.equals("2")) {
                msg1 = "You have been opted out yourself for the RSVP for the event<b> ${eventModel?.eventTitle} </b>."
                msg2 = "\n" +
                        " \n" +
                        "  We have unmarked you for the same."
                /* <b> ${eventModel?.eventTitle}</b>.*/
                launchFragment(
                    RSVPAcknowledgeFragment.newInstance(msg1, msg2),
                    "RSVPAcknowledgeFragment"
                )
            }
        })

        mRSVPViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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


        mRSVPViewModel.getProgressState()?.observe(viewLifecycleOwner,
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
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgLogo.setOnClickListener {
            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }
        mIncludedRSVPDetailBinding = mBinding.eventDetailRsvp
        mIncludedRSVPDetailBinding.imgDetailrsvpIntersted.setOnClickListener{

            mIncludedRSVPDetailBinding.imgDetailrsvpIntersted.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pulse))
            mIncludedRSVPDetailBinding.imgDetailrsvpIntersted.isClickable=false

            Handler(Looper.getMainLooper()).postDelayed({
                var mRSVPRequestModel = RSVPRequestModel()
                mRSVPRequestModel.EventID = eventModel!!.eventID
                mRSVPRequestModel.EventFeedbackID = 1
                mRSVPRequestModel.remainingGuestPasses=mRemainingGuestPasses
                mRSVPViewModel.getRSVP(mSessionManager.getAuthorization(), mRSVPRequestModel)
                mIncludedRSVPDetailBinding.imgDetailrsvpIntersted.isClickable = true
                mIncludedRSVPDetailBinding.imgDetailrsvpIntersted.clearAnimation()

            }, 2000)

        }

        mIncludedRSVPDetailBinding.imgDetailrsvpNotintersted.setOnClickListener{

            mIncludedRSVPDetailBinding.imgDetailrsvpNotintersted.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pulse))
            mIncludedRSVPDetailBinding.imgDetailrsvpNotintersted.isClickable=false


            Handler(Looper.getMainLooper()).postDelayed({

            var mRSVPRequestModel = RSVPRequestModel()
            mRSVPRequestModel.EventID = eventModel!!.eventID
            mRSVPRequestModel.EventFeedbackID = 2
            mRSVPRequestModel.remainingGuestPasses=mRemainingGuestPasses
            mRSVPViewModel.getRSVP(mSessionManager.getAuthorization(), mRSVPRequestModel)
            mIncludedRSVPDetailBinding.imgDetailrsvpNotintersted.isClickable = true
            mIncludedRSVPDetailBinding.imgDetailrsvpNotintersted.clearAnimation()
        }, 2000)

        }
        mBinding.buyTextView.setOnClickListener {

            activity?.let {

                if(mSessionManager.getProfileModel()!!.paymentMethodID) {

                    val intent = Intent(it, PayActivity::class.java)
                    intent.putExtra("amount",  eventModel!!.price)
                    intent.putExtra("itemType", "E")
                    intent.putExtra("itemId", eventModel!!.eventID!!)
                    intent.putExtra("memberId", mSessionManager.getUserDetailLoginModel()?.memberID)
                    intent.putExtra("authorization", mSessionManager.getAuthorization())
                    intent.putExtra("imageName", eventModel!!.imageName)
                    intent.putExtra("eventTitle", eventModel!!.eventTitle)
                    it.startActivity(intent)


                }else{
                    val intent = Intent(it, CheckoutActivityJava::class.java)
                    intent.putExtra("amount", eventModel!!.price)
                    intent.putExtra("itemType", "E")
                    intent.putExtra("itemId", eventModel!!.eventID!!)
                    intent.putExtra("memberId", mSessionManager.getUserDetailLoginModel()?.memberID)
                    intent.putExtra("authorization", mSessionManager.getAuthorization())
                    intent.putExtra("email", mSessionManager.getProfileModel()!!.email)
                    intent.putExtra("description",mSessionManager.getProfileModel()!!.description)
                    intent.putExtra("address",mSessionManager.getProfileModel()!!.address)
                    intent.putExtra("name",mSessionManager.getProfileModel()!!.firstName)
                    intent.putExtra("auth",mSessionManager.getAuthorization())
                    it.startActivity(intent)
                }
            }

        }
    }


    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {

        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.event_detail,
            container,
            false
        )

        setIncludedLayout()
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(context)!!
        mRSVPViewModel = ViewModelProvider(this, RSVPFactory(activity?.application)).get(
            RSVPViewModel::class.java
        )
    }



    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

    private fun createDynamicallyTextView(agentName: String,linearlayout : LinearLayout) {
        val myTextViews = arrayOfNulls<TextView>(agentName.length)


        val charsList: List<Char> = agentName.toList()

        for (i in charsList.indices) {
            val rowTextView = TextView(requireContext())
            // set some properties of rowTextView or something
            rowTextView.text = charsList[i].toString()
            // add the textview to the linearlayout
            linearlayout.addView(rowTextView)
            rowTextView.background = resources.getDrawable(R.drawable.event_bg, null)
            rowTextView.setTextColor(resources.getColor(R.color.black))
            val lparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) // Width , height
            rowTextView.gravity = Gravity.CENTER;
            rowTextView.layoutParams = lparams
            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
            rowTextView.setPadding(10,2,10,2)
            rowTextView.setTypeface(rowTextView.typeface, Typeface.BOLD)
            // save a reference to the textview for later
            myTextViews[i] = rowTextView
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(event: EventsResponseModel?) =

        EventDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_EVENT, event)
            }
        }
    }
   
}