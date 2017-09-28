package com.nextdots.retargetly.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.nextdots.retargetly.R;

public class DialogGpsUtils {

    static AlertDialog alertDialog = null;

    public static void openDialogSettings(final Activity activity){
       alertDialog = new AlertDialog.Builder(activity)
                .setMessage(activity.getResources().getString(R.string.alert_location))
                .setCancelable(false)
                .setPositiveButton(activity.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(callGPSSettingIntent);
                    }
                }).show();
    }

    public static void closeDialogSettings(){
        if(alertDialog!=null)
            alertDialog.dismiss();
    }
}
