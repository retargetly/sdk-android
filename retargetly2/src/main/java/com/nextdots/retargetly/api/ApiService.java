package com.nextdots.retargetly.api;

import com.google.gson.JsonElement;
import com.nextdots.retargetly.data.models.Event;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @Headers({
            "Content-Type: application/json"
    })

    @POST("android")
    Call<Void> callEvent(@Body Event event);

    @GET("sdk/params")
    Call<JsonElement> callInit(@Query("source_hash") String source_hash);

    @GET()
    Call<String> getStringResponse(@Url String url);

}
