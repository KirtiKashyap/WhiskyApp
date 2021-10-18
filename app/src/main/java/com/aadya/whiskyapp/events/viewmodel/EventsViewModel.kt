package com.aadya.whiskyapp.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.events.model.EventsResponseModel
import com.aadya.whiskyapp.utils.AlertModel

class EventsViewModel(eventsRepository: EventsRepository) : ViewModel() {
    private var eventsRepository: EventsRepository = eventsRepository

    fun getEvents(authorization: String?) {
        eventsRepository.getEvents(authorization)
   }

    fun getEventsObserver() : MutableLiveData<List<EventsResponseModel>?> {
        return  eventsRepository.getEventsState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return eventsRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return eventsRepository.getAlertViewState()
    }

    fun getprofileUnAuthorized() : LiveData<Boolean>{
        return  eventsRepository.getprofileUnAuthorized()
    }

}