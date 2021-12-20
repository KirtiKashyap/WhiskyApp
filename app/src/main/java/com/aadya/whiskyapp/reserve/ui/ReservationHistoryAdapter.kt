package com.aadya.whiskyapp.reserve.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.ItemReservationLogBinding
import com.aadya.whiskyapp.reserve.model.ReserveInfoResponse

class ReservationHistoryAdapter (
    context: Context
) : RecyclerView.Adapter<ReservationHistoryAdapter.MyViewHolder>() {

    private val reserveHistoryLog: ArrayList<ReserveInfoResponse> =
        ArrayList()
    private val context: Context

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

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        val reservationHistoryLog: ReserveInfoResponse = reserveHistoryLog[i]
        with(viewHolder) {

            binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_y)
            if(reservationHistoryLog.bookingStatus.equals("Cancelled")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_r)
                binding.statusTextView.text= "Activated: No"
            }else if(reservationHistoryLog.bookingStatus.equals("Approved")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_g)
                binding.statusTextView.text="Activated: Yes"
            }else if(reservationHistoryLog.bookingStatus.equals("Declined")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_o)
                binding.statusTextView.text= "Activated: No"
            }else if(reservationHistoryLog.bookingStatus.equals("Pending")){
                binding.mainLayout.setBackgroundResource(R.drawable.reserve_bg_y)
                binding.statusTextView.text= "Activated: No"
            }
            binding.noTextView.text="Number of people: "+reservationHistoryLog.numberofPeople
            binding.timeTextView.text=reservationHistoryLog.bookingTime
            binding.dateTextView.text=reservationHistoryLog.bookingDate
            binding.orderTextView.text="Order: "+reservationHistoryLog.notes
            binding.sNo.text=(i+1).toString()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemReservationLogBinding.bind(itemView)
    }

    init {
        this.reserveHistoryLog.addAll(reserveHistoryLog)
        this.context = context
    }

    override fun getItemCount(): Int {
        return  reserveHistoryLog.size
    }
}