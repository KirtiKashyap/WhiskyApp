package com.aadya.whiskyapp.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class EventAttendingViewModel(eventAttendingRepository: EventAttendingRepository) : ViewModel() {
    private var eventAttendingRepository: EventAttendingRepository = eventAttendingRepository

    fun getEventAttendingList(authorization: String?) {
        eventAttendingRepository.getEventAttendingList(authorization)
   }

    fun getEventAttendingListObserver() : MutableLiveData<List<EventsResponseModel>?> {
        return  eventAttendingRepository.getEventsAttendingState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return eventAttendingRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return eventAttendingRepository.getAlertViewState()
    }

    fun getEventAttendingUnAuthorized() : LiveData<Boolean>{
        return  eventAttendingRepository.getEventAttendingUnAuthorized()
    }

}