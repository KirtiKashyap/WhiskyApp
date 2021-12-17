package com.aadya.whiskyapp.scanlog.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.ItemScanLogBinding
import com.aadya.whiskyapp.scanlog.model.ScanLogResponse

class ScanLogAdapter(context: Context) : RecyclerView.Adapter<ScanLogAdapter.MyViewHolder>(){
    private val scanLogList: ArrayList<ScanLogResponse> =
        ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_scan_log, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val scanLog: ScanLogResponse = scanLogList[position]
        with(viewHolder) {
            binding.mainLayout.setBackgroundResource(R.drawable.eventattendingselected_bg)
            binding.dateTextViewTextView.text=scanLog.scanDate
            binding.timeTextView.text=scanLog.scanTime
            binding.sNoTextView.text=(position+1).toString()
        }
    }

    override fun getItemCount(): Int {
        return  scanLogList.size
    }
    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemScanLogBinding.bind(itemView)
    }
    fun notifyData(scanList: List<ScanLogResponse>) {
        this.scanLogList.clear()
        this.scanLogList.addAll(scanList)
        notifyDataSetChanged()
    }


}