package com.nextdots.retargetly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import com.nextdots.retargetly.api.ApiConstanst;
import com.nextdots.retargetly.utils.RetargetlyUtils;

import static android.content.Context.LOCATION_SERVICE;

public class GPSBroadCastReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        final LocationManager manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            RetargetlyUtils.LogR( "GPS ENABLE");
            sendBroadcastActive();
        } else {
            RetargetlyUtils.LogR("GPS DISABLE");
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