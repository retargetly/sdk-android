package com.nextdots.retargetly.data.models;

import com.google.gson.Gson;
import com.nextdots.retargetly.api.ApiConstanst;


import java.util.HashMap;
import java.util.Map;

public class Event {

    private String et;
    private String uid;
    private Map value;
    private String source_hash;
    private String app;
    private String mf;
    private String device;
    private String lan;
    private String apps;
    private Object val;
    private String appn;
    public String ip;
    private String ct;

    public Event(String uid, String source_hash, String app, String mf, String device, String lan,
                 String apps, String appn, String ct){
        this.et  = ApiConstanst.EVENT_OPEN;
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
        this.apps = apps;
        this.appn = appn;
        this.uid= uid;
        this.ct = ct;
    }

    public Event(String uid, String et, Map value, String source_hash, String app, String mf, String device,
                 String lan, String appn, String ct){
        this.et  = et;
        this.value = value;
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
        this.appn = appn;
        this.uid= uid;
        this.ct = ct;
    }

    public Event(String uid, String et, String latitude, String longitude, String accuracy,
                 String alt,
                 String source_hash, String app,
                 String mf, String device, String lan, String nwifi, String appn, String ct){
        this.et  = et;

        final HashMap<String, String> rPosition = new HashMap<>();
        rPosition.put("lat",String.valueOf(latitude) );
        rPosition.put("lng",String.valueOf(longitude));
        rPosition.put("accuracy", String.valueOf(accuracy));
        rPosition.put("alt", String.valueOf(alt));
        rPosition.put("nwifi", nwifi);
        this.value = rPosition;
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
        this.appn = appn;
        this.uid= uid;
        this.ct = ct;
    }


    public String getEt() {
        return et;
    }

    public String getValue(){return value.toString();}
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}