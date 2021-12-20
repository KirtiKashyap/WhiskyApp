package com.aadya.whiskyapp.scanlog.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aadya.whiskyapp.menu.viewmodel.MenuRepository;
import com.aadya.whiskyapp.menu.viewmodel.MenuViewModel;


public class ScanLogFactory implements ViewModelProvider.Factory {

    private Application application;

    public ScanLogFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ScanLogViewModel.class))
            return (T) new ScanLogViewModel(new ScanLogRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
