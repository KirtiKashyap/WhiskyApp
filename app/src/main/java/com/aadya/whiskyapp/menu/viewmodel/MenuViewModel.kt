package com.aadya.whiskyapp.menu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.menu.model.MenuResponse
import com.aadya.whiskyapp.utils.AlertModel

class MenuViewModel(menuRepository: MenuRepository): ViewModel() {

    private var menuRepository: MenuRepository = menuRepository

    fun getMenu(authorization: String?) {
        menuRepository.getMenuData(authorization)
    }

    fun getEventsObserver() : MutableLiveData<MenuResponse?> {
        return  menuRepository.getMenuState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return menuRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return menuRepository.getAlertViewState()
    }

    fun getprofileUnAuthorized() : LiveData<Boolean> {
        return  menuRepository.getprofileUnAuthorized()
    }
}