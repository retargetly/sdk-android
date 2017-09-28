package com.nextdots.retargetly;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
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
import android.widget.Toast;
import com.nextdots.retargetly.api.ApiConstanst;
import com.nextdots.retargetly.api.ApiController;
import com.nextdots.retargetly.data.models.Event;
import com.nextdots.retargetly.utils.DialogGpsUtils;
import com.nextdots.retargetly.utils.RetargetlyUtils;

import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static com.nextdots.retargetly.api.ApiConstanst.TAG;


public class Retargetly implements Application.ActivityLifecycleCallbacks, LocationListener {

    static public Application application = null;

    private boolean isFirst = false;
    private boolean hasSendCoordinate = false;

    static public String pid;
    static public String uid;

    private String manufacturer;
    private String model;
    private String idiome;

    private boolean forceGPS = false;

    private Activity currentActivity;

    private ApiController apiController;

    public static void init(Application application, String uid, String pid){
        new Retargetly(application,uid,pid);
    }

    public static void init(Application application, String uid, String pid, boolean forceGPS){
        new Retargetly(application,uid,pid,forceGPS);
    }

    private Retargetly(Application application, String uid, String pid){
        this.application = application;
        this.manufacturer   = Build.MANUFACTURER;
        this.model          = Build.MODEL;
        this.idiome         = Locale.getDefault().getLanguage();
        this.uid       = uid;
        this.pid       = pid;
        this.application.registerActivityLifecycleCallbacks(this);
        apiController  = new ApiController();
    }

    private Retargetly(Application application, String uid, String pid, boolean forceGPS){
        this.application = application;
        this.manufacturer   = Build.MANUFACTURER;
        this.model          = Build.MODEL;
        this.idiome         = Locale.getDefault().getLanguage();
        this.uid       = uid;
        this.pid       = pid;
        this.application.registerActivityLifecycleCallbacks(this);
        this.forceGPS = forceGPS;
        apiController  = new ApiController();
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

            isFirst = true;
            apiController.callCustomEvent(new Event(uid, application.getPackageName(), pid, manufacturer, model, idiome, RetargetlyUtils.getInstalledApps(application)));
            Log.d(TAG, "First Activity " + activity.getClass().getSimpleName());

        } else {

            apiController.callCustomEvent(new Event(ApiConstanst.EVENT_CHANGE, activity.getClass().getSimpleName(), uid, application.getPackageName(), pid, manufacturer, model, idiome));
            Log.d(TAG, "Activity " + activity.getClass().getSimpleName());

        }

        if(forceGPS)
            RetargetlyUtils.checkPermissionGps(activity);

        if(!hasSendCoordinate)
            callCoordinateGps(activity);

        if(currentActivity != activity) {

            currentActivity = activity;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();

                fm.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentResumed(FragmentManager fm, Fragment f) {

                        super.onFragmentResumed(fm, f);
                        Log.d(TAG, "Fragment: " + f.getClass().getSimpleName());
                        apiController.callCustomEvent(new Event(ApiConstanst.EVENT_CHANGE, f.getClass().getSimpleName(), uid, application.getPackageName(), pid, manufacturer, model, idiome));

                    }
                }, false);

            } else {

                android.app.FragmentManager fm = activity.getFragmentManager();

                fm.registerFragmentLifecycleCallbacks(new android.app.FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentResumed(android.app.FragmentManager fm, android.app.Fragment f) {

                        super.onFragmentResumed(fm, f);
                        Log.d(TAG, "Fragment: " + f.getClass().getSimpleName());
                        apiController.callCustomEvent(new Event(ApiConstanst.EVENT_CHANGE, f.getClass().getSimpleName(), uid, application.getPackageName(), pid, manufacturer, model, idiome));

                    }
                }, false);

            }
        }else{

            apiController.callCustomEvent(new Event(uid, application.getPackageName(), pid, manufacturer, model, idiome, RetargetlyUtils.getInstalledApps(application)));
            Log.d(TAG, "Active Activity " + activity.getClass().getSimpleName());

        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        DialogGpsUtils.closeDialogSettings();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        DialogGpsUtils.closeDialogSettings();
        hasSendCoordinate = false;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        DialogGpsUtils.closeDialogSettings();
        hasSendCoordinate = false;
    }

    private void callCoordinateGps(Activity activity){
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
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"GPS onLocationChanged");
        if((location.getLatitude()!=0 && location.getLongitude()!=0) && !hasSendCoordinate) {
            hasSendCoordinate = true;
            Log.d(TAG, "Latitude: " + location.getLatitude());
            Log.d(TAG, "Longitude: " + location.getLongitude());
            RetargetlyUtils.callEventCoordinate(location.getLatitude()+"",location.getLongitude()+"");
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
}
