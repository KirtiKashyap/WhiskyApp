package com.aadya.whiskyapp.reserve.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.ItemReservationLogBinding
import com.aadya.whiskyapp.reserve.model.ReserveInfoResponse
import kotlin.collections.ArrayList

class ReservationHistoryAdapter(
    context: Context,
    reservationItemClick: ReservationItemClick
) : RecyclerView.Adapter<ReservationHistoryAdapter.MyViewHolder>() {
    private val reserveHistoryLog: ArrayList<ReserveInfoResponse> =
        ArrayList()
    private val context: Context
    private val reservationItemClick: ReservationItemClick
    fun notifyData(purchaseHistoryList: List<ReserveInfoResponse>) {
        this.reserveHistoryLog.clear()
        this.reserveHistoryLog.addAll(purchaseHistoryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {

        val itemView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_reservation_log, viewGroup, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        val reservationHistoryLog: ReserveInfoResponse = reserveHistoryLog[i]

        with(viewHolder) {
            binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_y)
            if(reservationHistoryLog.bookingStatus.equals("Cancelled")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_r)
            }else if(reservationHistoryLog.bookingStatus.equals("Approved")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_g)
            }else if(reservationHistoryLog.bookingStatus.equals("Declined")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_o)
            }else if(reservationHistoryLog.bookingStatus.equals("Pending")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_y)

            }
            try {
                val splitString = reservationHistoryLog.visitedDatetime!!
                    .split(" ").toTypedArray()
                binding.statusTextView.text = reservationHistoryLog.bookingStatus
                binding.statusTimeTextView.text = splitString[1]
            }catch (e: Exception){
                e.printStackTrace()
            }
            binding.noTextView.text="Number of people: "+reservationHistoryLog.numberofPeople
            binding.timeTextView.text=reservationHistoryLog.bookingTime
            binding.dateTextView.text=reservationHistoryLog.bookingDate
            binding.orderTextView.text="Order: "+reservationHistoryLog.notes
            binding.reasonTextView.text="Note: "+reservationHistoryLog.otherDescription
            binding.sNo.text=(i+1).toString()

            binding.mainLayout.setOnClickListener {
               if(reserveHistoryLog[i].bookingStatus.equals("Approved")) {
                   reservationItemClick.onItemClick(reserveHistoryLog[i])
               }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    interface ReservationItemClick {
        fun onItemClick(mReserveInfoResponse: ReserveInfoResponse)
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemReservationLogBinding.bind(itemView)
    }

    init {
        this.reserveHistoryLog.addAll(reserveHistoryLog)
        this.context = context
        this.reservationItemClick = reservationItemClick
    }

    override fun getItemCount(): Int {
        return  reserveHistoryLog.size
    }
}