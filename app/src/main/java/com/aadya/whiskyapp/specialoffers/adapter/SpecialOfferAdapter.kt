package com.aadya.whiskyapp.specialoffers.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.specialoffers.ui.SpecialOfferFragment
import com.aadya.whiskyapp.utils.CommonUtils


class SpecialOfferAdapter(
    fragmentManager: FragmentManager,
    specialOfferList: List<SpecialOfferResponseModel>?,
    isFromDialog: Boolean) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val specialOfferList: ArrayList<SpecialOfferResponseModel>? = ArrayList<SpecialOfferResponseModel>()
    private lateinit var mCommonUtils: CommonUtils
    private val isFromDialog=isFromDialog
    override fun getItem(position: Int): Fragment {

        return SpecialOfferFragment.newInstance(this.specialOfferList?.get(position),isFromDialog)


    }

    override fun getCount(): Int {
        return specialOfferList?.size!!
    }



    init {
        if (!specialOfferList.isNullOrEmpty()){
            this.specialOfferList?.clear()
            this.specialOfferList?.addAll(specialOfferList)
            mCommonUtils = CommonUtils
        }


    }

}



