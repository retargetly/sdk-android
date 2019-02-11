package com.nextdots.retargetly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nextdots.retargetly.api.ApiController;
import com.nextdots.retargetly.utils.RetargetlyUtils;

public class NetworkBroadCastReceiver extends BroadcastReceiver {

    public static String nWifi = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null) {
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if(null != ni && NetworkInfo.State.CONNECTING!=ni.getState() && NetworkInfo.State.CONNECTED==ni.getState()){
                nWifi = RetargetlyUtils.getCurrentSsid(context);
            }
        }
    }

    public static void getIp(Context context, boolean isGetIpEnabled, ApiController.ListenerSendInfo listenerSendInfo){
        if(isGetIpEnabled){
            final ApiController apiController = new ApiController();
            apiController.callIp(listenerSendInfo);
            nWifi = RetargetlyUtils.getCurrentSsid(context);
        }else{
            nWifi = RetargetlyUtils.getCurrentSsid(context);
            listenerSendInfo.finishRequest();
        }
    }
}
