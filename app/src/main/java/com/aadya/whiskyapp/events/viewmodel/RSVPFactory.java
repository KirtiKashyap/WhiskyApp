package com.aadya.whiskyapp.events.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RSVPFactory implements ViewModelProvider.Factory {

    private Application application;

    public RSVPFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RSVPViewModel.class))
            return (T) new RSVPViewModel(new RSVPRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
