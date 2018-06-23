package com.nextdots.retargetly.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.nextdots.retargetly.Retargetly;
import com.nextdots.retargetly.api.ApiConstanst;
import com.nextdots.retargetly.api.ApiController;
import com.nextdots.retargetly.data.listeners.CustomEventListener;
import com.nextdots.retargetly.data.models.Event;
import com.nextdots.retargetly.receivers.NetworkBroadCastReceiver;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.LOCATION_SERVICE;
import static com.nextdots.retargetly.api.ApiConstanst.TAG;

public class RetargetlyUtils {

    public static void callCustomEvent(Map value) {
        callEvent(value, null);
    }

    public static void callCustomEvent(Map value, CustomEventListener customEventListener) {
        callEvent(value, customEventListener);
    }

    public static void callEventCoordinate(String latitude, String longitude,
                                           String accuracy, String alt) {
        ApiController apiController = new ApiController();

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String idiome = Locale.getDefault().getLanguage();

        apiController.callCustomEvent(new Event(getUID(), ApiConstanst.EVENT_GEO, latitude, longitude,
                accuracy, alt,
                Retargetly.source_hash, Retargetly.application.getPackageName(), manufacturer,
                model, idiome,
                NetworkBroadCastReceiver.nWifi.replaceAll("\"", ""),
                getAppName(Retargetly.application)));
    }

    public static void callEventDeeplink(final String url) {
        try {
            final ApiController apiController = new ApiController();
            final String manufacturer = Build.MANUFACTURER;
            final String model = Build.MODEL;
            final String idiome = Locale.getDefault().getLanguage();
            final Map<String, String> map = new HashMap<>();
            map.put("url", url);
            if (ApiController.ip != null && ApiController.ip.length() > 0) {
                apiController.callCustomEvent(
                        new Event(getUID(),
                                ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                Retargetly.application.getPackageName(), manufacturer, model,
                                idiome, getAppName(Retargetly.application))
                        , null);
            } else {
                apiController.callIp(new ApiController.ListenerSendInfo() {
                    @Override
                    public void finishRequest() {
                        apiController.callCustomEvent(
                                new Event(getUID(),
                                        ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                        Retargetly.application.getPackageName(), manufacturer,
                                        model, idiome, getAppName(Retargetly.application))
                                , null);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void callEventDeeplink(final String url, final CustomEventListener listener) {
        try {
            final ApiController apiController = new ApiController();
            final String manufacturer = Build.MANUFACTURER;
            final String model = Build.MODEL;
            final String idiome = Locale.getDefault().getLanguage();
            final Map<String, String> map = new HashMap<>();
            map.put("url", url);
            if (ApiController.ip != null && ApiController.ip.length() > 0) {
                apiController.callCustomEvent(
                        new Event(getUID(),
                                ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                Retargetly.application.getPackageName(), manufacturer, model,
                                idiome, getAppName(Retargetly.application))
                        , listener);
            } else {
                apiController.callIp(new ApiController.ListenerSendInfo() {
                    @Override
                    public void finishRequest() {
                        apiController.callCustomEvent(
                                new Event(getUID(),
                                        ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                        Retargetly.application.getPackageName(), manufacturer,
                                        model, idiome, getAppName(Retargetly.application))
                                , listener);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void callEvent(Map value, CustomEventListener customEventListener) {
        ApiController apiController = new ApiController();

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String idiome = Locale.getDefault().getLanguage();

        apiController.callCustomEvent(
                new Event(getUID(),
                        ApiConstanst.EVENT_CUSTOM, value, Retargetly.source_hash,
                        Retargetly.application.getPackageName(), manufacturer, model, idiome,
                        getAppName(Retargetly.application))
                , customEventListener);
    }

    public static String getAppName(Application application) {
        return application.getPackageManager()
                .getApplicationLabel(application.getApplicationInfo()).toString();
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


    public static boolean hasLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    public static void checkPermissionGps(Activity activity) {
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

    public static String getCurrentSsid(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo.isConnected()) {
                final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    return connectionInfo.getSSID();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getUID() {
        return  ApiController.UUID;
    }

    public static void LogR(String descripcion){
        Log.d(TAG,descripcion);
    }

}