package com.nextdots.retargetly.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nextdots.retargetly.Retargetly;
import com.nextdots.retargetly.data.listeners.CustomEventListener;
import com.nextdots.retargetly.data.models.Event;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {

    private ApiService service;

    public ApiController(){

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Accept", "Application/JSON")
                                        .addHeader("android_hash", Retargetly.android_hash).build();
                                return chain.proceed(request);
                            }
                        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.retargetly.com/")
                .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public void callCustomEvent(Event event){
        callEvent(event,null);
    }

    public void callCustomEvent(Event event,final CustomEventListener customEventListener){
        callEvent(event,customEventListener);
    }

    private void callEvent(final Event event,final CustomEventListener customEventListener){
        service.callEvent(event).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(event.getEt() != ApiConstanst.EVENT_OPEN)
                    Log.d(ApiConstanst.TAG,"Event : "+event.getEt() + ", value:" + event.getValue() + ", status: " + response.code());
                else
                    Log.d(ApiConstanst.TAG,"Event : "+event.getEt() + ", status: " + response.code());

                if(customEventListener != null)
                    customEventListener.customEventSuccess();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(ApiConstanst.TAG,t.getMessage());
                if(customEventListener != null)
                    customEventListener.customEventFailure(t.getMessage());
            }
        });
    }

}
