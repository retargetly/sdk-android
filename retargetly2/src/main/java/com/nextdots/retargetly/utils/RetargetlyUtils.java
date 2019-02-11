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
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nextdots.retargetly.BuildConfig;
import com.nextdots.retargetly.Retargetly;
import com.nextdots.retargetly.api.ApiConstanst;
import com.nextdots.retargetly.api.ApiController;
import com.nextdots.retargetly.data.listeners.CustomEventListener;
import com.nextdots.retargetly.data.models.Event;
import com.nextdots.retargetly.data.models.RetargetlyParams;
import com.nextdots.retargetly.receivers.NetworkBroadCastReceiver;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.LOCATION_SERVICE;
import static com.nextdots.retargetly.api.ApiConstanst.TAG;

public class RetargetlyUtils {

    private static RetargetlyParams params;
    private static Context context;

    public static void injectParam(Context context1, RetargetlyParams params1){
        context = context1;
        params = params1;
    }

    public static void callCustomEvent(Map value) {
        callEvent(value, null);
    }

    public static void callCustomEvent(Map value, CustomEventListener customEventListener) {
        callEvent(value, customEventListener);
    }

    public static void callEventCoordinate( String latitude, String longitude,
                                           String accuracy, String alt) {
        ApiController apiController = new ApiController();

        String manufacturer = RetargetlyUtils.getManufacturer();
        String model = RetargetlyUtils.getDeviceName();
        String language = RetargetlyUtils.getLanguage();
        String nWifi = RetargetlyUtils.getNameWifi();

        apiController.callCustomEvent(new Event(getUID(), ApiConstanst.EVENT_GEO, latitude, longitude,
                accuracy, alt,
                Retargetly.source_hash, Retargetly.application.getPackageName(), manufacturer,
                model, language, nWifi,
                getAppName(Retargetly.application), RetargetlyUtils.getCurrentCountryIso()));
    }

    public static void callEventDeeplink( final String url) {
        try {
            final ApiController apiController = new ApiController();
            final String manufacturer = params.isSendManufacturerEnabled()? Build.MANUFACTURER: null;
            final String model = params.isSendDeviceNameEnabled()? Build.MODEL: null;
            final String idiome = params.isSendLanguageEnabled() ? Locale.getDefault().getLanguage() : null;
            final Map<String, String> map = new HashMap<>();
            map.put("url", url);
            if(!params.isSendIpEnabled()){
                apiController.callCustomEvent(
                        new Event(getUID(),
                                ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                Retargetly.application.getPackageName(), manufacturer, model,
                                idiome, getAppName(Retargetly.application),
                                RetargetlyUtils.getCurrentCountryIso())
                        , null);
            }else if (ApiController.ip != null && ApiController.ip.length() > 0) {
                apiController.callCustomEvent(
                        new Event(getUID(),
                                ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                Retargetly.application.getPackageName(), manufacturer, model,
                                idiome, getAppName(Retargetly.application),
                                RetargetlyUtils.getCurrentCountryIso())
                        , null);
            } else {
                apiController.callIp(new ApiController.ListenerSendInfo() {
                    @Override
                    public void finishRequest() {
                        apiController.callCustomEvent(
                                new Event(getUID(),
                                        ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                        Retargetly.application.getPackageName(), manufacturer,
                                        model, idiome, getAppName(Retargetly.application),
                                        RetargetlyUtils.getCurrentCountryIso()),
                                null);
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
            final String manufacturer = RetargetlyUtils.getManufacturer();
            final String model = RetargetlyUtils.getDeviceName();
            final String language = RetargetlyUtils.getLanguage();
            final Map<String, String> map = new HashMap<>();
            map.put("url", url);
            if (ApiController.ip != null && ApiController.ip.length() > 0) {
                apiController.callCustomEvent(
                        new Event(getUID(),
                                ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                Retargetly.application.getPackageName(), manufacturer, model,
                                language, getAppName(Retargetly.application),
                                RetargetlyUtils.getCurrentCountryIso())
                        , listener);
            } else {
                apiController.callIp(new ApiController.ListenerSendInfo() {
                    @Override
                    public void finishRequest() {
                        apiController.callCustomEvent(
                                new Event(getUID(),
                                        ApiConstanst.EVENT_DEEPLINK, map, Retargetly.source_hash,
                                        Retargetly.application.getPackageName(), manufacturer,
                                        model, language, getAppName(Retargetly.application),
                                        RetargetlyUtils.getCurrentCountryIso())
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

        String manufacturer = Retargetly.params.isSendManufacturerEnabled() ? Build.MANUFACTURER : null;
        String model = Retargetly.params.isSendDeviceNameEnabled() ? Build.MODEL : null;
        String language = Retargetly.params.isSendLanguageEnabled() ? Locale.getDefault().getLanguage() : null;

        apiController.callCustomEvent(
                new Event(getUID(),
                        ApiConstanst.EVENT_CUSTOM, value, Retargetly.source_hash,
                        Retargetly.application.getPackageName(), manufacturer, model, language,
                        getAppName(Retargetly.application),
                        RetargetlyUtils.getCurrentCountryIso()),
                        customEventListener);
    }

    public static String getAppName(Application application) {
        return application.getPackageManager()
                .getApplicationLabel(application.getApplicationInfo()).toString();
    }

    public static String getInstalledApps(Application application) {
        if(params.isSendApplicationsEnabled()){
            String result = "";
            List<PackageInfo> packs = application.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);
                if ((!isSystemPackage(p))) {
                    result += p.applicationInfo.loadLabel(application.getPackageManager()).toString() + ", ";
                }
            }
            return result;
        }else{
            return null;
        }
    }

    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }


    public static boolean hasLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
        if (BuildConfig.DEBUG) {
            Log.d(TAG, descripcion);
        }
    }

    public static String getCurrentCountryIso(){
        if(params.isSendCountryEnabled()){
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm!=null ? tm.getSimCountryIso() : null;
        }else{
            return null;
        }
    }

    public static String getManufacturer() {
        return params.isSendManufacturerEnabled() ? Build.MANUFACTURER : null;
    }

    public static String getDeviceName() {
        return params.isSendDeviceNameEnabled() ? Build.MODEL : null;
    }

    public static String getLanguage() {
        return params.isSendLanguageEnabled() ? Locale.getDefault().getLanguage() : null;
    }

    public static String getNameWifi() {
        return params.isSendNameWifiEnabled() && (NetworkBroadCastReceiver.nWifi!= null) ?
                NetworkBroadCastReceiver.nWifi.replaceAll("\"", "") : null;
    }
}