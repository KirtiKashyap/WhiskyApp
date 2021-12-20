package com.aadya.whiskyapp.scanlog.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aadya.whiskyapp.scanlog.model.ScanLogRequest
import com.aadya.whiskyapp.scanlog.model.ScanLogResponse
import com.aadya.whiskyapp.utils.AlertModel

class ScanLogViewModel(scanLogRepository: ScanLogRepository) : ViewModel() {
    private var scanLogRepository: ScanLogRepository = scanLogRepository

    fun getScanLog(authorization: String?, scanLogRequest: ScanLogRequest) {
        scanLogRepository.getScanLog(authorization, scanLogRequest)
    }

    fun getScanLogObserver(): MutableLiveData<List<ScanLogResponse>?> {
        return scanLogRepository.getScanLogState()
    }

    fun getProgressState(): LiveData<Int?>? {
        return scanLogRepository.getProgressState()
    }

    fun getAlertViewState(): LiveData<AlertModel?>? {
        return scanLogRepository.getAlertViewState()
    }

    fun getprofileUnAuthorized(): LiveData<Boolean> {
        return scanLogRepository.getprofileUnAuthorized()
    }
}