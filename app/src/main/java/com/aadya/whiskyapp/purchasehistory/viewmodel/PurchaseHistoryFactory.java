package com.aadya.whiskyapp.purchasehistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aadya.whiskyapp.events.viewmodel.EventAttendingRepository;
import com.aadya.whiskyapp.events.viewmodel.EventAttendingViewModel;

public class PurchaseHistoryFactory implements ViewModelProvider.Factory {

    private Application application;

    public PurchaseHistoryFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PurchaseHistoryViewModel.class))
            return (T) new PurchaseHistoryViewModel(new PurchaseHistoryRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
