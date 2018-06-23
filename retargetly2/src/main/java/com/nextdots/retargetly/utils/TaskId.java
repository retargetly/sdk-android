package com.nextdots.retargetly.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.nextdots.retargetly.api.ApiController;

import java.io.IOException;

public class TaskId extends AsyncTask<Void, Void, String> {

    private final Application context;
    private final ApiController.ListenerSendInfo listenerSendInfo;
    private final SharedPreferences sharedPreferences;

    public TaskId(Application context, ApiController.ListenerSendInfo listenerSendInfo){
        this.context = context;
        this.listenerSendInfo = listenerSendInfo;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }

    @Override
    protected String doInBackground(Void... voids) {

        final String temp_uuid = sharedPreferences.getString("UUID", "");
        if(temp_uuid.length()==0 || temp_uuid.equalsIgnoreCase("")) {
            AdvertisingIdClient.Info adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);

            } catch (IOException e) {
                e.printStackTrace();

            } catch (GooglePlayServicesNotAvailableException e) {
                // Google Play services is not available entirely.
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }
            if (adInfo != null) {
                final String uuid = adInfo.getId();
                final SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("UUID", uuid);
                editor.apply();
                editor.commit();
                return uuid;
            }else return "";
        }else
            return temp_uuid;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ApiController.UUID = s;
        RetargetlyUtils.LogR("UUID "+s);
        listenerSendInfo.finishRequest();
    }
}
