package com.nextdots.retargetly.api;

import com.nextdots.retargetly.data.models.Event;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Content-Type: application/json"
    })

    @POST("android")
    Call<Void> callEvent(@Body Event event);

}
