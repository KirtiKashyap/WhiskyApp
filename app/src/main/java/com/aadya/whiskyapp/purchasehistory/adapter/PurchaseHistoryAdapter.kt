package com.aadya.whiskyapp.purchasehistory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.AdapterPurchasehistoryListBinding
import com.aadya.whiskyapp.purchasehistory.model.PurchaseHistory
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager


class PurchaseHistoryAdapter(
    context: Context,
    purchaseHistoryItemClick: PurchaseHistoryListItemClick
) : RecyclerView.Adapter<PurchaseHistoryAdapter.MyViewHolder>() {

    private val purchaseHistoryList: ArrayList<PurchaseHistory> =
        ArrayList()
    private val context: Context
    private val purchaseHistoryItemClick: PurchaseHistoryListItemClick
    private lateinit var mSessionManager: SessionManager
    private lateinit var mCommonUtils: CommonUtils

    fun notifyData(purchaseHistoryList: List<PurchaseHistory>) {
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
        val purchaseHistory: PurchaseHistory = purchaseHistoryList[i]
        with(viewHolder) {
            binding.tvPurchaseTitle.text="Status: "+purchaseHistory.paymentStatus
            binding.tvAmt.text="Amount: "+purchaseHistory.amount
            binding.tvDate.text="Date: "+purchaseHistory.paymentDate
            binding.tvOrderid.text="Ticket Id: "+purchaseHistory.ticketID
            
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