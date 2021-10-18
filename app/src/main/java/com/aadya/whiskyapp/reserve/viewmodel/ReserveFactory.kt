package com.aadya.whiskyapp.reserve.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReserveFactory(private val application: Application?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReserveViewModel::class.java)) ReserveViewModel(
            ReserveRepository(
                application!!
            )
        ) as T else throw IllegalArgumentException("Unknown ViewModel class")
    }
}
