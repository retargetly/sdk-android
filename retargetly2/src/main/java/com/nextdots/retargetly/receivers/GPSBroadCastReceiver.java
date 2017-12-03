package com.nextdots.retargetly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import com.nextdots.retargetly.api.ApiConstanst;

import static android.content.Context.LOCATION_SERVICE;
import static com.nextdots.retargetly.api.ApiConstanst.TAG;

public class GPSBroadCastReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        final LocationManager manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "GPS ENABLE");
            sendBroadcastActive();
        }
        else
        {
            Log.d(TAG,"GPS DISABLE");
            sendBroadcastDisable();
        }
    }

    public void sendBroadcastActive(){
        Intent i = new Intent(ApiConstanst.GPS_ENABLE);
        this.context.sendBroadcast(i);
    }

    public void sendBroadcastDisable(){
        Intent i = new Intent(ApiConstanst.GPS_DISABLE);
        this.context.sendBroadcast(i);
    }
}