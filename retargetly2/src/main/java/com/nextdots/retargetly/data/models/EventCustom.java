package com.nextdots.retargetly.data.models;

import com.nextdots.retargetly.Retargetly;

import java.util.Map;

/**
 * Created by luistundisi on 12/19/17.
 */

public class EventCustom {

    private String et;
    private String source_hash;
    private String app;
    private String mf;
    private String device;
    private String lan;
    private String apps;
    private Map<String,String> value;
    private String rPosition;
    private String uid;

    public EventCustom(String et, Map<String,String> value, String source_hash, String app, String mf, String device, String lan){
        this.et  = et;
        this.value = value;
        this.source_hash = source_hash;
        this.app = app;
        this.mf = mf;
        this.device = device;
        this.lan = lan;
        this.uid = Retargetly.android_id;
    }

    public Map getValue() {
        return value;
    }
}
