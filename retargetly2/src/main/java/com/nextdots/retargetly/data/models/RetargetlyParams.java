package com.nextdots.retargetly.data.models;


public class RetargetlyParams {
    private boolean sendGeoData;
    private boolean forceGPS;
    private Class classDeeplink;
    private String source_hash;
    private boolean isSendIpEnabled;
    private boolean isSendNameWifiEnabled;
    private boolean isSendLanguageEnabled;
    private boolean isSendManufacturerEnabled;
    private boolean isSendDeviceNameEnabled;
    private boolean isSendApplicationsEnabled;
    private boolean isSendCountryEnabled;

    public RetargetlyParams(Builder builder) {
        this.forceGPS = builder.forceGPS;
        this.sendGeoData = builder.sendGeoData;
        this.source_hash = builder.source_hash;
        this.classDeeplink = builder.classDeeplink;
        this.isSendIpEnabled = builder.isSendIpEnabled;
        this.isSendCountryEnabled = builder.isSendCountryEnabled;
        this.isSendNameWifiEnabled = builder.isSendNameWifiEnabled;
        this.isSendLanguageEnabled = builder.isSendLanguageEnabled;
        this.isSendDeviceNameEnabled = builder.isSendDeviceNameEnabled;
        this.isSendManufacturerEnabled = builder.isSendManufacturerEnabled;
        this.isSendApplicationsEnabled = builder.isSendApplicationsEnabled;
    }

    public boolean isSendGeoData() {
        return sendGeoData;
    }

    public void setSendGeoData(boolean sendGeoData) {
        this.sendGeoData = sendGeoData;
    }

    public boolean isForceGPS() {
        return forceGPS;
    }

    public void setForceGPS(boolean forceGPS) {
        this.forceGPS = forceGPS;
    }

    public Class getClassDeeplink() {
        return classDeeplink;
    }

    public void setClassDeeplink(Class classDeeplink) {
        this.classDeeplink = classDeeplink;
    }

    public String getSourceHash() {
        return source_hash;
    }

    public boolean isSendIpEnabled() {
        return isSendIpEnabled;
    }

    public boolean isSendNameWifiEnabled() {
        return isSendNameWifiEnabled;
    }

    public boolean isSendLanguageEnabled() {
        return isSendLanguageEnabled;
    }

    public boolean isSendDeviceNameEnabled() {
        return isSendDeviceNameEnabled;
    }

    public boolean isSendManufacturerEnabled() {
        return isSendManufacturerEnabled;
    }

    public boolean isSendApplicationsEnabled() {
        return isSendApplicationsEnabled;
    }

    public boolean isSendCountryEnabled() {
        return isSendCountryEnabled;
    }

    public static class Builder {
        private String source_hash;
        private boolean sendGeoData;
        private boolean forceGPS;
        private Class classDeeplink;
        private boolean isSendIpEnabled;
        private boolean isSendLanguageEnabled;
        private boolean isSendNameWifiEnabled;
        private boolean isSendDeviceNameEnabled;
        private boolean isSendManufacturerEnabled;
        private boolean isSendApplicationsEnabled;
        private boolean isSendCountryEnabled;

        public Builder(String source_hash) {
            this.source_hash = source_hash;
            this.forceGPS = true;
            this.sendGeoData = true;
            this.isSendIpEnabled = true;
            this.isSendCountryEnabled = true;
            this.isSendNameWifiEnabled = true;
            this.isSendLanguageEnabled = true;
            this.isSendDeviceNameEnabled = true;
            this.isSendManufacturerEnabled = true;
            this.isSendApplicationsEnabled = true;
        }

        public Builder sendGeoData(boolean sendGeoData) {
            this.sendGeoData = sendGeoData;
            return this;
        }

        public Builder forceGPS(boolean forceGPS) {
            this.forceGPS = forceGPS;
            return this;
        }

        public Builder isSendIpEnabled(boolean isSendIpEnabled) {
            this.isSendIpEnabled = isSendIpEnabled;
            return this;
        }

        public Builder isSendNameWifiEnabled(boolean isSendNameWifiEnabled) {
            this.isSendNameWifiEnabled = isSendNameWifiEnabled;
            return this;
        }

        public Builder isSendLanguageEnabled(boolean isSendLanguageEnabled) {
            this.isSendLanguageEnabled = isSendLanguageEnabled;
            return this;
        }

        public Builder isSendDeviceNameEnabled(boolean isSendDeviceNameEnabled) {
            this.isSendDeviceNameEnabled = isSendDeviceNameEnabled;
            return this;
        }

        public Builder isSendManufacturerEnabled(boolean isSendManufacturerEnabled) {
            this.isSendManufacturerEnabled = isSendManufacturerEnabled;
            return this;
        }

        public Builder isSendApplicationsEnabled(boolean isSendApplicationsEnabled) {
            this.isSendApplicationsEnabled = isSendApplicationsEnabled;
            return this;
        }

        public Builder isSendCountryEnabled(boolean isSendCountryEnabled) {
            this.isSendCountryEnabled = isSendCountryEnabled;
            return this;
        }

        public Builder classDeeplink(Class classDeeplink) {
            this.classDeeplink = classDeeplink;
            return this;
        }

        public Builder sendOptionalParams(boolean enabled) {
            this.isSendIpEnabled = enabled;
            this.isSendCountryEnabled = enabled;
            this.isSendNameWifiEnabled = enabled;
            this.isSendLanguageEnabled = enabled;
            this.isSendDeviceNameEnabled = enabled;
            this.isSendManufacturerEnabled = enabled;
            this.isSendApplicationsEnabled = enabled;
            return this;
        }

        public RetargetlyParams build(){
            return new RetargetlyParams(this);
        }
    }
}
