package com.nextdots.retargetly.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.nextdots.retargetly.api.ApiConstanst;
import com.nextdots.retargetly.api.ApiController;
import com.nextdots.retargetly.data.listeners.CustomEventListener;
import com.nextdots.retargetly.data.models.Event;
import java.util.List;
import java.util.Locale;

public class RetargetlyUtils {

    public static void callCustomEvent(Application application, String value, String uid, int pid){
        callEvent(application,value,uid,pid,null);
    }

    public static void callCustomEvent(Application application, String value, String uid, int pid, CustomEventListener customEventListener){
        callEvent(application,value,uid,pid,customEventListener);
    }

    private static void callEvent(Application application, String value, String uid, int pid, CustomEventListener customEventListener){
        ApiController apiController  = new ApiController();

        String manufacturer   = Build.MANUFACTURER;
        String model          = Build.MODEL;
        String idiome         = Locale.getDefault().getLanguage();

        apiController.callCustomEvent(new Event(ApiConstanst.EVENT_CUSTOM, value , uid, application.getPackageName(), pid, manufacturer, model, idiome),customEventListener);
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

}
