package com.aadya.whiskyapp.profile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProfileFactory implements ViewModelProvider.Factory {

    private Application application;

    public ProfileFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class))
            return (T) new ProfileViewModel(new ProfileRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
