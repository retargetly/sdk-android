package com.nextdots.retargetly.data.models;

import com.nextdots.retargetly.api.ApiConstanst;

public class Event {

    private String et;
    private Value value;
    private String uid;
    private String app;
    private String mf;
    private String device;
    private String lan;
    private String apps;
    private String pid;
    private Object val;
    private String rPosition;

    public Event(String uid, String app, String pid, String mf, String device, String lan, String apps){
        this.et  = ApiConstanst.EVENT_OPEN;
        this.uid = uid;
        this.app = app;
        this.pid = pid;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
        this.apps = apps;
    }

    public Event(String et, String value, String uid, String app, String pid, String mf, String device, String lan){
        this.et  = et;
        this.value = new Value(value);
        this.uid = uid;
        this.app = app;
        this.pid = pid;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
    }

    public Event(String et, Object value, String uid, String app, String pid, String mf, String device, String lan){
        this.et  = et;
        this.val = value;
        this.uid = uid;
        this.app = app;
        this.pid = pid;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
    }

    public Event(String et, String latitude, String longitude, String uid, String app, String pid, String mf, String device, String lan){
        this.et  = et;
        this.rPosition = latitude+";"+longitude;
        this.uid = uid;
        this.app = app;
        this.pid = pid;
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
