package com.nextdots.retargetly.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import com.nextdots.retargetly.Retargetly;
import com.nextdots.retargetly.api.ApiConstanst;
import com.nextdots.retargetly.api.ApiController;
import com.nextdots.retargetly.data.listeners.CustomEventListener;
import com.nextdots.retargetly.data.models.Event;
import java.util.List;
import java.util.Locale;

public class RetargetlyUtils {

    public static void callCustomEvent(String value){
        callEvent(value,null);
    }

    public static void callCustomEvent(String value, CustomEventListener customEventListener){
        callEvent(value,customEventListener);
    }

    public static void callCustomEvent(Object value){
        callEvent(value,null);
    }

    public static void callCustomEvent(Object value, CustomEventListener customEventListener){
        callEvent(value,customEventListener);
    }

    public static void callEventCoordinate(String latitude, String longitude){
        ApiController apiController  = new ApiController();

        String manufacturer   = Build.MANUFACTURER;
        String model          = Build.MODEL;
        String idiome         = Locale.getDefault().getLanguage();

        apiController.callCustomEvent(new Event(ApiConstanst.EVENT_CUSTOM, latitude, longitude , Retargetly.uid, Retargetly.application.getPackageName(), Retargetly.pid, manufacturer, model, idiome));
    }

    private static void callEvent(Object value, CustomEventListener customEventListener){
        ApiController apiController  = new ApiController();

        String manufacturer   = Build.MANUFACTURER;
        String model          = Build.MODEL;
        String idiome         = Locale.getDefault().getLanguage();

        apiController.callCustomEvent(new Event(ApiConstanst.EVENT_CUSTOM, value , Retargetly.uid, Retargetly.application.getPackageName(), Retargetly.pid, manufacturer, model, idiome),customEventListener);
    }

    private static void callEvent(String value, CustomEventListener customEventListener){
        ApiController apiController  = new ApiController();

        String manufacturer   = Build.MANUFACTURER;
        String model          = Build.MODEL;
        String idiome         = Locale.getDefault().getLanguage();

        apiController.callCustomEvent(new Event(ApiConstanst.EVENT_CUSTOM, value , Retargetly.uid, Retargetly.application.getPackageName(), Retargetly.pid, manufacturer, model, idiome),customEventListener);
    }

    public static String getInstalledApps(Application application) {
        String result = "";
        List<PackageInfo> packs = application.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                result += p.applicationInfo.loadLabel(application.getPackageManager()).toString() + ", ";
            }
        }
        return result;
    }

    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }


    public static boolean hasLocationEnabled(Activity activity){
        LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }else{
            return false;
        }
    }

    public static void checkPermissionGps(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (!hasLocationEnabled(activity)) {
                DialogGpsUtils.openDialogSettings(activity);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }
    }
}
