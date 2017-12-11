package com.nextdots.retargetly.data.models;

import com.nextdots.retargetly.api.ApiConstanst;

public class Event {

    private String et;
    private Value value;
    private String source_hash;
    private String app;
    private String mf;
    private String device;
    private String lan;
    private String apps;
    private Object val;
    private String rPosition;

    public Event(String source_hash, String app, String mf, String device, String lan, String apps){
        this.et  = ApiConstanst.EVENT_OPEN;
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
        this.apps = apps;
    }

    public Event(String et, String value, String source_hash, String app, String mf, String device, String lan){
        this.et  = et;
        this.value = new Value(value);
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
    }

    public Event(String et, Object value, String source_hash, String app, String mf, String device, String lan){
        this.et  = et;
        this.val = value;
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
    }

    public Event(String et, String latitude, String longitude, String source_hash, String app, String mf, String device, String lan){
        this.et  = et;
        this.rPosition = latitude+";"+longitude;
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
    }

    public String getEt() {
        return et;
    }

    public String getValue() {
        return this.value != null ? this.value.named : (this.rPosition != null ? this.rPosition : "");
    }
}
