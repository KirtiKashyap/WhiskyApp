package com.aadya.whiskyapp.purchasehistory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.AdapterPurchasehistoryListBinding
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager


class PurchaseHistoryAdapter(
    context: Context,
    purchaseHistoryItemClick: PurchaseHistoryListItemClick
) : RecyclerView.Adapter<PurchaseHistoryAdapter.MyViewHolder>() {

    private val purchaseHistoryList: ArrayList<SpecialOfferResponseModel> =
        ArrayList<SpecialOfferResponseModel>()
    private val context: Context
    private val purchaseHistoryItemClick: PurchaseHistoryListItemClick
    private lateinit var mSessionManager: SessionManager
    private lateinit var mCommonUtils: CommonUtils

    fun notifyData(purchaseHistoryList: List<SpecialOfferResponseModel>) {
        this.purchaseHistoryList.clear()
        this.purchaseHistoryList.addAll(purchaseHistoryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {

        val itemView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_purchasehistory_list, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        val mSpecailOfferResponseModel: SpecialOfferResponseModel = purchaseHistoryList[i]
        with(viewHolder) {

            /* val eventdate_time =
                mCommonUtils.convertString_To_Date_ddMMMyyyyformat(mEventAttendingModel?.eventDate.toString())
            if (mEventAttendingModel.eventStartTime != null) {
                eventdate_time.plus("  $mEventAttendingModel.eventStartTime")
                binding.tvDateTime.text = eventdate_time
            } else
                binding.tvDateTime.text = eventdate_time

            if (mEventAttendingModel.isActive) {
                binding.tvEventStatus.text = "Completed"
                binding.eventAttendingMainLayout.setBackgroundResource(R.drawable.eventattendingunselected_bg )
            }
            else {
                binding.tvEventStatus.text = "UpComing Event"
                binding.eventAttendingMainLayout.setBackgroundResource(R.drawable.eventattendingselected_bg )
            }

            binding.tvEventTitle.text = mEventAttendingModel.eventTitle
            binding.tvEventAddress.text = mEventAttendingModel.eventLocation

            binding.eventAttendingMainLayout.setOnClickListener {
               *//*  selectedPosition=i
                notifyDataSetChanged()*//*
                eventAttendingItemClick.onItemClick(eventAttendingList.get(i))
            }*/
            
        }

    }




        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        interface PurchaseHistoryListItemClick {
            fun onItemClick(mSpecialOfferResponseModel: SpecialOfferResponseModel)
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binding = AdapterPurchasehistoryListBinding.bind(itemView)
        }

        init {
            this.purchaseHistoryList.addAll(purchaseHistoryList)
            this.context = context
            this.purchaseHistoryItemClick = purchaseHistoryItemClick
            mSessionManager = SessionManager.getInstance(context)!!
            mCommonUtils = CommonUtils
        }




    override fun getItemCount(): Int {
       return  purchaseHistoryList.size
    }
}