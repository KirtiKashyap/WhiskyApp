package com.aadya.whiskyapp.events.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.AdapterEventattendingListBinding
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager


class EventAttendingAdapter(
    context: Context,
    eventAttendingItemClick: EventAttendingListItemClick
) : RecyclerView.Adapter<EventAttendingAdapter.MyViewHolder>() {

    private val eventAttendingList: ArrayList<EventsResponseModel> =
        ArrayList<EventsResponseModel>()
    private val context: Context
    private val eventAttendingItemClick: EventAttendingListItemClick
    private lateinit var mSessionManager: SessionManager
    private lateinit var mCommonUtils: CommonUtils
    private var selectedPosition : Int = -1

    fun notifyData(eventAttendingList: List<EventsResponseModel>) {
        this.eventAttendingList.clear()
        this.eventAttendingList.addAll(eventAttendingList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {

        val itemView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_eventattending_list, viewGroup, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        val mEventAttendingModel: EventsResponseModel = eventAttendingList[i]
        with(viewHolder) {

             val eventdate_time =
                mCommonUtils.convertString_To_Date_ddMMMyyyyformat(mEventAttendingModel?.eventDate.toString())
            if (mEventAttendingModel.eventStartTime != null) {
                eventdate_time.plus("  $mEventAttendingModel.eventStartTime")
                binding.tvDateTime.text = "Date: $eventdate_time"
            } else
                binding.tvDateTime.text = "Date: $eventdate_time"

            if (mEventAttendingModel.isActive) {
                binding.tvEventStatus.text = "Completed"
                binding.eventAttendingMainLayout.setBackgroundResource(R.drawable.eventattendingunselected_bg )
            }
            else {
                binding.tvEventStatus.text = "UpComing Event"
                binding.eventAttendingMainLayout.setBackgroundResource(R.drawable.eventattendingselected_bg )
            }

            binding.tvEventTitle.text = mEventAttendingModel.eventTitle
            binding.tvEventAddress.text = "Address: "+mEventAttendingModel.eventLocation
            binding.tvAvailPass.text="Avail Pass: "+mEventAttendingModel.availGuestPasses
            binding.eventAttendingMainLayout.setOnClickListener {
               /*  selectedPosition=i
                notifyDataSetChanged()*/
                eventAttendingItemClick.onItemClick(eventAttendingList.get(i))
            }
            
          /*  if(selectedPosition == i)
                binding.eventAttendingMainLayout.setBackgroundResource(R.drawable.eventattendingselected_bg )
            else
                binding.eventAttendingMainLayout.setBackgroundResource(R.drawable.eventattendingunselected_bg )*/
        }

    }




        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        interface EventAttendingListItemClick {
            fun onItemClick(mEventResponseModel: EventsResponseModel)
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binding = AdapterEventattendingListBinding.bind(itemView)
        }

        init {
            this.eventAttendingList.addAll(eventAttendingList)
            this.context = context
            this.eventAttendingItemClick = eventAttendingItemClick
            mSessionManager = SessionManager.getInstance(context)!!
            mCommonUtils = CommonUtils
        }




    override fun getItemCount(): Int {
       return  eventAttendingList.size
    }
}