package com.aadya.whiskyapp.specialoffers.ui

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentSpecialofferBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.payment.ui.Payment_Activity
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.utils.*
import kotlinx.android.synthetic.main.fragment_specialoffer.*

private const val ARG_PARAM1 = "param1"
class SpecialOfferFragment : Fragment(), Animator.AnimatorListener {
    private lateinit var mBinding: FragmentSpecialofferBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private var mDrawerInterface: DrawerInterface? = null

    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    var onButtonSwipeDownListener: onButtonSwipeDownListner? = null
    var onEventsPageSwipeUpListner: onEventsPageSwipeUpListner? = null
    private var param1: SpecialOfferResponseModel? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)

        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        handleObserver()
        setUi()


        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buy_button.setOnClickListener {
            activity?.let{
                val intent = Intent (it, Payment_Activity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun setUi() {
        if(param1!=null){
            mBinding.tvSpecialofferName.text = param1?.title
            mBinding.tvSpecialofferAmt.text = "$"+param1?.price.toString()
            mBinding.tvSpecialofferCode.text = param1?.yo.toString() + "YO"
        }

    }

    private fun handleObserver() {

        onButtonSwipeDownListener?.getSwipeDownViewState()?.observe(viewLifecycleOwner, Observer {


            mBinding.buyButton.animate()
                .translationY(mBinding.buyButton.height.toFloat())
                .alpha(1.0f)
                .setListener(this)

        })


    }


    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {


        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_specialoffer,
            container,
            false
        )
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(requireContext())!!
        setIncludedLayout()

        //onButtonSwipeDownListener = onButtonSwipeDownListner(requireContext(), mBinding.buyButton)

        onEventsPageSwipeUpListner = onEventsPageSwipeUpListner(requireContext(), mBinding.constraintSpecialoffer)
        onEventsPageSwipeUpListner?.getLiveData()?.observe(viewLifecycleOwner, Observer {

            val ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.replace(
                R.id.app_container,
                SpecialofferDetailFragment.newInstance(param1),
                "SpecialofferDetailFragment"
            )
            ft?.addToBackStack(null)
            ft?.commit()

        })
    }

    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainheader
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
        fun newInstance(specialOfferModel: SpecialOfferResponseModel?) =
            SpecialOfferFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, specialOfferModel)
                }
            }


    }




    override fun onAnimationStart(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {


        var msg =
            "You have agreed to Purchase <b> Samantha  Curtis - Rock Oyster,</b> of this amount  $145 and would be debited from your credit card and would be delivered to your address by 1st Nov 2021."
        launchFragment(
            ConfirmPurchaseFragment.newInstance(msg, ""),
            "ConfirmPurchaseFragment"
        )
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }


}