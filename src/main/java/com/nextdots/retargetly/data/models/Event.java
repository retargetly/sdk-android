package com.nextdots.retargetly.data.models;

public class Event {

    private String et;
    private String value;
    private String uid;
    private String app;
    private String mf;
    private String device;
    private String lan;
    private String apps;

    private int pid;

    public Event(String et, String uid, String app, int pid, String mf, String device, String lan, String apps){
        this.et  = et;
        this.uid = uid;
        this.app = app;
        this.pid = pid;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
        this.apps = apps;
    }

    public Event(String et, String value, String uid, String app, int pid, String mf, String device, String lan){
        this.et  = et;
        this.value = value;
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
        return value;
    }
}
