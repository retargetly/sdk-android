package com.nextdots.retargetly;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.nextdots.retargetly.api.ApiConstanst;
import com.nextdots.retargetly.api.ApiController;
import com.nextdots.retargetly.data.models.Event;
import com.nextdots.retargetly.receivers.NetworkBroadCastReceiver;
import com.nextdots.retargetly.utils.RetargetlyUtils;

import java.util.Locale;


import static android.content.Context.LOCATION_SERVICE;
import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.nextdots.retargetly.api.ApiConstanst.TAG;


public class Retargetly implements Application.ActivityLifecycleCallbacks, LocationListener {

    static public Application application = null;

    private boolean isFirst = false;
    private boolean hasSendCoordinate = false;
    private boolean sendGeoData = true;

    static public String source_hash;

    private String manufacturer;
    private String model;
    private String idiome;

    private boolean forceGPS = false;

    private Activity currentActivity;

    private ApiController apiController;

    public static void init(Application application, String source_hash) {
        new Retargetly(application, source_hash);
    }

    public static void init(Application application, String source_hash, boolean forceGPS) {
        new Retargetly(application, source_hash, forceGPS);
    }

    public static void init(Application application, String source_hash, boolean forceGPS, boolean sendGeoData) {
        new Retargetly(application, source_hash, forceGPS, sendGeoData);
    }

    private Retargetly(Application application, String source_hash) {
        this.application = application;
        this.manufacturer = Build.MANUFACTURER;
        this.model = Build.MODEL;
        this.idiome = Locale.getDefault().getLanguage();
        this.source_hash = source_hash;
        this.application.registerActivityLifecycleCallbacks(this);
        apiController = new ApiController();
        getDefaultParams();
    }

    private Retargetly(Application application, String source_hash, boolean forceGPS) {
        this.application = application;
        this.manufacturer = Build.MANUFACTURER;
        this.model = Build.MODEL;
        this.idiome = Locale.getDefault().getLanguage();
        this.source_hash = source_hash;
        this.application.registerActivityLifecycleCallbacks(this);
        this.forceGPS = forceGPS;
        apiController = new ApiController();
        getDefaultParams();
    }

    private Retargetly(Application application, String source_hash, boolean forceGPS, boolean sendGeoData) {
        this.application = application;
        this.manufacturer = Build.MANUFACTURER;
        this.model = Build.MODEL;
        this.idiome = Locale.getDefault().getLanguage();
        this.source_hash = source_hash;
        this.application.registerActivityLifecycleCallbacks(this);
        this.forceGPS = forceGPS;
        this.sendGeoData = sendGeoData;
        apiController = new ApiController();
        getDefaultParams();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!isFirst) {
            hasPermission(activity);
            isFirst = true;
            sendOpenEvent();
            Log.d(TAG, "First Activity " + activity.getClass().getSimpleName());
        }
        sendGeoEvent(application);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        hasSendCoordinate = false;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        hasSendCoordinate = false;
    }

    private void callCoordinateGps(Context activity) {
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

        long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

        LocationManager manager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                LocationManager locationManager = (LocationManager) activity
                        .getSystemService(LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "GPS onLocationChanged");
        if ((location.getLatitude() != 0 && location.getLongitude() != 0) && !hasSendCoordinate && sendGeoData) {
            hasSendCoordinate = true;
            Log.d(TAG, "Latitude: " + location.getLatitude());
            Log.d(TAG, "Longitude: " + location.getLongitude());
            Log.d(TAG, "Send geo event");
            RetargetlyUtils.callEventCoordinate(
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    private void getDefaultParams() {
        if (sendGeoData && RetargetlyUtils.hasLocationEnabled(application))
            apiController.callInitData(source_hash);
        getIp();
    }

    private void getIp(){
        IntentFilter intentFilter = new IntentFilter(CONNECTIVITY_ACTION);
        application.registerReceiver(new NetworkBroadCastReceiver(),
                intentFilter);
    }

    private void sendOpenEvent(){
        Log.d(TAG, "Send open event");
        apiController.callCustomEvent(new Event(source_hash, application.getPackageName(), manufacturer, model, idiome, RetargetlyUtils.getInstalledApps(application)));
    }

    private void sendGeoEvent(Context context){
        if (!hasSendCoordinate && sendGeoData)
            callCoordinateGps(context);
    }

    private void hasPermission(Activity activity){
        if (sendGeoData && forceGPS)
            RetargetlyUtils.checkPermissionGps(activity);
    }

}
