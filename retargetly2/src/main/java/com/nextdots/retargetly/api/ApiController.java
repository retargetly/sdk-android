package com.nextdots.retargetly.api;

import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nextdots.retargetly.data.listeners.CustomEventListener;
import com.nextdots.retargetly.data.models.Event;
import com.nextdots.retargetly.utils.RetargetlyUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiController {

    private ApiService service;
    /*
    Creamos e inicializamos las variables con sus valores por default
     */
    public static int motionFrequency = 300; //Frecuencia en movimiento
    public static int staticFrequency = 1800; //Frecuencia en reposo
    public static int motionTreshold = 300; // Distancia
    public static int motionDetectionFrequency = 120;
    public static String ip = "";
    public static String UUID ="";

    public ApiController() {

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Accept", "Application/JSON").build();
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

    public void callCustomEvent(Event event) {
        event.ip = ip;
        callEvent(event, null);
    }

    public void callCustomEvent(Event event, final CustomEventListener customEventListener) {
        event.ip = ip;
        callEvent(event, customEventListener);
    }

    public void callIp(final ListenerSendInfo listener){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://icanhazip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        final ApiService service = retrofit.create(ApiService.class);
        service.getStringResponse("https://icanhazip.com/").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    ip= response.body();
                    if(ip!=null)
                        ip = ip.replaceAll("\n","");
                }
                if (listener != null)
                    listener.finishRequest();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                if (listener != null)
                    listener.finishRequest();
            }
        });
    }

    public void callInitData(String api, final ListenerSendInfo listenerSendInfo) {
        RetargetlyUtils.LogR("https://api.retargetly.com/sdk/params?source_hash="+api);
        service.callInit(api).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    final JsonObject json = response.body().getAsJsonObject();
                    if (json != null) {
                        final JsonObject jsonResponse = json.getAsJsonObject("response");
                        if (jsonResponse != null) {
                            if(jsonResponse
                                    .has("motionDetectionFrequency"))
                                motionDetectionFrequency = jsonResponse
                                        .get("motionDetectionFrequency").getAsInt();
                            if(jsonResponse
                                    .has("motionFrequency"))
                                motionFrequency = jsonResponse
                                        .get("motionFrequency").getAsInt();
                            if(jsonResponse
                                    .has("motionThreshold"))
                                motionTreshold = jsonResponse
                                        .get("motionThreshold").getAsInt();
                            if(jsonResponse
                                    .has("staticFrequency"))
                                staticFrequency = jsonResponse
                                        .get("staticFrequency").getAsInt();
                        }
                    }
                }
                listenerSendInfo.finishRequest();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                listenerSendInfo.finishRequest();
                t.printStackTrace();
            }


        });

    }

    private void callEvent(final Event event, final CustomEventListener customEventListener) {
        RetargetlyUtils.LogR("json -> "+event.toString());
        service.callEvent(event).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!event.getEt().equalsIgnoreCase(ApiConstanst.EVENT_OPEN))
                    RetargetlyUtils.LogR("Event : " + event.getEt() + ", " +
                            "value:" + event.getValue() + ", " +
                            "status: " + response.code() + ", " +
                            "IP: " + event.ip);
                else
                    RetargetlyUtils.LogR("Event : " + event.getEt() + ", status: " + response.code() + ", " +
                            "IP: " + event.ip );

                if (customEventListener != null)
                    customEventListener.customEventSuccess();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (customEventListener != null)
                    customEventListener.customEventFailure(t.getMessage());
            }
        });
    }

    public interface ListenerSendInfo {
        void finishRequest();
    }

}
