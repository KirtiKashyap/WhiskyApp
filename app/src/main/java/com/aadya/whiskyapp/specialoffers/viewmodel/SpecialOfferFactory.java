package com.aadya.whiskyapp.specialoffers.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aadya.whiskyapp.profile.viewmodel.ProfileRepository;
import com.aadya.whiskyapp.profile.viewmodel.ProfileViewModel;

public class SpecialOfferFactory implements ViewModelProvider.Factory {

    private Application application;

    public SpecialOfferFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SpecialOfferViewModel.class))
            return (T) new SpecialOfferViewModel(new SpecialOfferRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
