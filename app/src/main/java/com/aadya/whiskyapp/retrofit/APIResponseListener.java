package com.aadya.whiskyapp.retrofit;

import retrofit2.Response;

public interface APIResponseListener {
    void onSuccess(Response response);

    void onFailure();
}
