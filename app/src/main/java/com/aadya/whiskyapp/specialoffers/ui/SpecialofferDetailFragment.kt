package com.aadya.whiskyapp.specialoffers.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.transition.TransitionInflater
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentSpecialofferDetailBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
private const val ARG_PARAM1 = "param1"
class SpecialofferDetailFragment : Fragment() {

    private lateinit var mBinding: FragmentSpecialofferDetailBinding
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private var mDrawerInterface: DrawerInterface? = null
    private var param1: SpecialOfferResponseModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        return mBinding.root
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {


        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_specialoffer_detail,
            container,
            false
        )
        mCommonUtils = CommonUtils
        setIncludedLayout()
        setUI()
    }

    private fun setUI() {
        if(param1!=null){
            mBinding.tvSpecialofferName.text = param1?.title
            mBinding.tvSpecialofferAmt.text = param1?.price.toString()
            mBinding.tvSpecialofferCode.text = param1?.yo.toString() + "YO"
            mBinding.tvSpecialofferDetail.text = param1?.description
        }
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
            SpecialofferDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, specialOfferModel)
                }
            }
    }
}