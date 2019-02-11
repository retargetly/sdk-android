![Retargetly](https://github.com/retargetly/sdk-android/blob/master/rely_artboard.png)

# Retargetly

Retargetly is a tracking library for Android.

### Prerequisites

```
Android Studio
JDK
```

# Important

If your application uses fragments for full compatibility with the library, we recommend creating fragments with

### Api >= 26

```java

getFragmentManager()

```

### Api < 26

```java

getSupportFragmentManager()

```
### Min SDK

15 (Android 4.0.3–4.0.4 Ice Cream Sandwich)

### Installing

To get a Git project into your build:

Add following lines in the Manifest: 
```xml
<manifest ... >
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    ...
</manifest>
```
Add it in the root build.gradle at the end of repositories:

```gradle
allprojects {
  repositories {
    ...
    jcenter { url "http://jcenter.bintray.com/" }
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency

```gradle
dependencies {
  implementation 'com.github.retargetly:sdk-android:2.1.0'
}
```

## Usage

You must create a class that extends of application and in the oncreate add the following line

```Retargetly.init(this,source_hash);```

### Example

```java
public class App extends Application {

    String source_hash = "19N10-F&!Xazt";

    @Override
    public void onCreate() {
        super.onCreate();
        Retargetly.init(this,source_hash);
    }
}
```
### Send Geo Data 
If you want to send geodata events in the application: 
```Retargetly.init(this,source_hash, false, true);```
#### Example

```java

public class App extends Application {

    String source_hash = "19N10-F&!Xazt";
    boolean forceGps= true;
    boolean sendGeoData = true;
    @Override
    public void onCreate() {
        super.onCreate();
        Retargetly.init(this,source_hash, forceGps, sendGeoData);
    }
}
```

### Force GPS

If you want show permission dialog do you set true ForceGPS:
```Retargetly.init(this,source_hash, true, true);```

### Example

```java
public class App extends Application {

    String source_hash = "19N10-F&!Xazt";
    boolean forceGps= true;
    boolean sendGeoData = true;
    @Override
    public void onCreate() {
        super.onCreate();
        Retargetly.init(this,source_hash, forceGps, sendGeoData);
    }
}
```

### Data user

Now you can manage which parameters are sent to the server. For use reported user info level you need use the RetargeltlyParam.Builder.

### Example

```java
public class App extends Application {
    String source_hash = "19N10-F&!Xazt";
    @Override
    public void onCreate() {
        super.onCreate();
        RetargetlyParams params = new RetargetlyParams.Builder(source_hash)
                                                       .sendOptionalParams(false)
                                                       .build();
        Retargetly.init(this,params);
    }
}
```
where the sendOptionalParams option is a function that adds all the parameters options, which are language, manufacturer, deviceName, ipAddress, applications, wifiName and country.

In addition all parameters can be disable individually using the RetargeltyParam.Builder. In case you want to disable IP parameter, you can always do it like this:

### Example

```java
RetargetlyParams params = new RetargetlyParams.Builder(source_hash)
                                            .isSendIpEnabled(false)
                                            .build();
```

### Builder Parameters

![Retargetly](https://github.com/retargetly/sdk-android/blob/master/retargetlyparams.png)

### Deeplinks
#### Send Deeplink event without response
```java
RetargetlyUtils.callEventDeeplink(Map<String,String> value);
```
#### Send Deeplink event with response
```java
RetargetlyUtils.callEventDeeplink(Map<String,String> value, CustomEventListener customEventListener);
```
#### Detect Deeplink
If you want the SDK to detect when a deeplink is opened, you must set in the init the class received deeplinks, for example:  
```java
Retargetly.init(this,source_hash, myclassdeeplinks.class, false, true);
```
If you not set your class Deeplink in the init, the SDK detect automatically in your first opened activity.

### Custom Event

#### Custom event map without response

```java
RetargetlyUtils.callCustomEvent(Map<String,String> value);
```
#### Custom event map with response

```java
RetargetlyUtils.callCustomEvent(Map<String,String> value, CustomEventListener customEventListener);
```

#### Response

```java
public interface CustomEventListener {
    void customEventSuccess();
    void customEventFailure(String msg);
}
```

### Custom Events Example

#### Custom event map without response

```java
Map obj = new HashMap();
obj.put("Event","Test Event");
RetargetlyUtils.callCustomEvent(obj);

```

#### Custom event with response

```java
public class MainActivity extends AppCompatActivity implements CustomEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map obj = new HashMap();
        obj.put("Event","Test Event");
        RetargetlyUtils.callCustomEvent(obj, this);
    }

    @Override
    public void customEventSuccess() {
        Toast.makeText(getApplication(),"Custom event send",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void customEventFailure(String msg) {
        Toast.makeText(getApplication(),msg,Toast.LENGTH_SHORT).show();
    }
}
```

#### Util getInstalledApps

```java
String result = RetargetlyUtils.getInstalledApps(getApplication());

// result -> "Amazon, Trello, Toggle, Facebook, ...."
```

## Logs

### First Activity

```xml
D/Retargetly -: First Activity MainActivity

//Success
D/Retargetly -: Event : open, status: 200

//Failure
D/Retargetly -: Event : open, 500
```

### Change Activity or Fragment

```xml
D/Retargetly -: Activity MainActivity
D/Retargetly -: Fragment MainFragment

//Success
D/Retargetly -: Event : change, value:MainActivity, status: 200

//Failure
D/Retargetly -: Event : change, 500
```

### Custom Event

```xml
//Success
D/Retargetly -: Event : Custom , value:{Event=Test event}, status: 200

//Failure
D/Retargetly -: Event : custom, 500
```

## Built With

* [Android Studio](https://developer.android.com/) - Ide development

## Versioning

For the versions available, see the [tags on this repository](https://github.com/retargetly/sdk-android.git).


## Which is the information that the SDK sends to the DMP?

```xml
- Device ID: anonymous advertising identificator, the ones provided by Google and Apple.
- Type of event: which is the event that triggered the data reception, it may be application open, custom event (if configured), geo event (if activated), or deeplink (if configured).
- Custom Data: this is only for custom events. Custom events send custom data in key/value format.
- Lat/Long/Accuracy (if geo is active): when gps data is being tracked on the SDK, geo events are being sent to the DMP.
- Installed apps: only on open events. It sends a list of installed apps on the device (only works for android).
- Manufacturer: device manufacturer name.
- Device: celular model.
- Application: which is the current application that is sending the data.
- Language: which is the device configured language.
- Ip: which is the IP address of the device.
- Wifi Name: which is the name of the wifi that the user is connected to.
- Country: iso code 2 of the country where the device is located.
```