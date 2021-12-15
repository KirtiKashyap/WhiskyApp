package com.aadya.whiskyapp.menu.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aadya.whiskyapp.events.viewmodel.EventsRepository;
import com.aadya.whiskyapp.events.viewmodel.EventsViewModel;


public class MenuFactory implements ViewModelProvider.Factory {

    private Application application;

    public MenuFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MenuViewModel.class))
            return (T) new MenuViewModel(new MenuRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
