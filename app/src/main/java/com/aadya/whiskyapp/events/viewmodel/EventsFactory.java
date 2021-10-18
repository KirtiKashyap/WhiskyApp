package com.aadya.whiskyapp.events.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aadya.whiskyapp.profile.viewmodel.ProfileRepository;
import com.aadya.whiskyapp.profile.viewmodel.ProfileViewModel;

public class EventsFactory implements ViewModelProvider.Factory {

    private Application application;

    public EventsFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventsViewModel.class))
            return (T) new EventsViewModel(new EventsRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
