package com.aadya.whiskyapp.payment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PaymentFactory implements ViewModelProvider.Factory{
    private Application application;

    public PaymentFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PaymentUpdateViewModel.class))
            return (T) new PaymentUpdateViewModel(new PaymentRepository(application));
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

