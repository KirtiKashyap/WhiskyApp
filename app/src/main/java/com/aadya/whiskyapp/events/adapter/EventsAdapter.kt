package com.aadya.whiskyapp.events.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.events.ui.EventsFragment
import com.aadya.whiskyapp.events.ui.deletePageViewPager
import com.aadya.whiskyapp.events.ui.updateEventsViewPager


class EventsAdapter(
    fragmentManager: FragmentManager,
    mdeletePageViewPager: deletePageViewPager,
    eventsList: ArrayList<EventsResponseModel>,
    mUpdateEventsViewPager: updateEventsViewPager,
    isFromDialog: Boolean
) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val eventList = ArrayList<EventsResponseModel>()
    private var mdeletePageViewPager: deletePageViewPager? = null
    private var mUpdateEventsViewPager: updateEventsViewPager? = null
    private val isFromDialog=isFromDialog


    init {
        this.mdeletePageViewPager = mdeletePageViewPager
        this.mUpdateEventsViewPager = mUpdateEventsViewPager
        this.eventList.clear()
        this.eventList.addAll(eventsList)
    }


    fun setItems(items: List<EventsResponseModel>) {
        this.eventList.clear()
        this.eventList.addAll(items)
        notifyDataSetChanged()
    }


    fun notifyData(position: Int) {
        if (this.eventList.size == 1) {
            this.eventList.removeAt(position)
            notifyDataSetChanged()
            mUpdateEventsViewPager?.updateEventsViewPager()

        } else {
            this.eventList.removeAt(position)
            notifyDataSetChanged()
        }

    }

    override fun getCount(): Int {
        return eventList.size
    }

    override fun getItem(position: Int): Fragment {
        return EventsFragment.newInstance(eventList[position], position, mdeletePageViewPager,isFromDialog)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}