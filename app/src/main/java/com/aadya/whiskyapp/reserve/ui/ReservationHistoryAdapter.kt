package com.aadya.whiskyapp.reserve.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.ItemReservationHistoryBinding
import com.aadya.whiskyapp.purchasehistory.model.PurchaseHistory
import com.aadya.whiskyapp.specialoffers.model.SpecialOfferResponseModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager

class ReservationHistoryAdapter (
    context: Context,
    purchaseHistoryItemClick: PurchaseHistoryListItemClick
) : RecyclerView.Adapter<ReservationHistoryAdapter.MyViewHolder>() {

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
            .inflate(R.layout.item_reservation_history, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        val purchaseHistory: PurchaseHistory = purchaseHistoryList[i]
        with(viewHolder) {
            binding.tvPurchaseTitle.text=purchaseHistory.title
            binding.tvStatus.text="Status: "+purchaseHistory.paymentStatus
            binding.tvAmt.text="Amount: $"+purchaseHistory.amount+".00"
            binding.tvDate.text="Purchased Date: "+purchaseHistory.paymentDate
            binding.tvExpectedDate.text="Expected Date of Delivery: "+purchaseHistory.deliveryDate
            binding.purchaseHistoryMainLayout.setBackgroundResource(R.drawable.eventattendingselected_bg )
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
        val binding = ItemReservationHistoryBinding.bind(itemView)
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