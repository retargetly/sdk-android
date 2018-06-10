package com.nextdots.retargetly.utils;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class GeoUtils {

    public static final String TAG = GeoUtils.class.getName();
    private static final int milliseconds = 1000;
    private Timer timer;
    private Handler handler;
    private TimerTask task;
    private long milliSeconds;
    private ListenerTask listener;
    public GeoUtils(long milliSeconds, ListenerTask listener){
        this.milliSeconds = milliSeconds;
        this.listener = listener;
    }

    public void initCount(){
        if(handler==null)
            handler = new Handler();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.next();
                    }
                });
            }
        };
        timer = new Timer(true);
        timer.schedule(task, milliSeconds, milliSeconds);
    }

    public void cancelCount(){
        if(timer!=null){
            timer.cancel();
        }
    }

    public static int SecondsToMilliseconds(int seconds) {
        return seconds * milliseconds;
    }
    public interface ListenerTask{
        void next();
    }
}
