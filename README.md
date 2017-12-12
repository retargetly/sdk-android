![Retargetly](http://beta.retargetly.com/wp-content/uploads/2015/07/Logo.png)

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

### Installing

To get a Git project into your build:

Add it in the root build.gradle at the end of repositories:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency

```gradle
dependencies {
  compile 'com.github.retargetly:sdk-android:1.0.6.1'
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

### Force GPS

if you want to force the gps in the application

```Retargetly.init(this,source_hash,true);```

### Example

```java
public class App extends Application {

    String source_hash = "19N10-F&!Xazt";
    boolean forceGps = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Retargetly.init(this,source_hash,forceGps);
    }
}
```

Forcing gps forces the user to activate the gps with a dialog that goes directly to the location permissions

### Broadcast if GPS is enable/disable

```java

@Override
protected void onResume() {
    super.onResume();
    registerReceiver(gpsEnable, new IntentFilter(ApiConstanst.GPS_ENABLE));
    registerReceiver(gpsDisable, new IntentFilter(ApiConstanst.GPS_DISABLE));
}

@Override
protected void onPause() {
    super.onPause();
    unregisterReceiver(gpsEnable);
    unregisterReceiver(gpsDisable);
}

public BroadcastReceiver gpsEnable = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"GPS ENABLED");
    }
};

public BroadcastReceiver gpsDisable = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"GPS DISABLED");
    }
};
```

### Custom Event

#### Custom event string without response

```java
RetargetlyUtils.callCustomEvent(String value);
```
#### Custom event string with response

```java
RetargetlyUtils.callCustomEvent(String value, CustomEventListener customEventListener);
```

#### Custom event object without response

```java
RetargetlyUtils.callCustomEvent(Object value);
```
#### Custom event object with response

```java
RetargetlyUtils.callCustomEvent(Object value, CustomEventListener customEventListener);
```

#### Response

```java
public interface CustomEventListener {
    void customEventSuccess();
    void customEventFailure(String msg);
}
```

### Custom Events Example

#### Custom event without response

```java

RetargetlyUtils.callCustomEvent("Custom Event");

```

#### Custom event with response

```java
public class MainActivity extends AppCompatActivity implements CustomEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RetargetlyUtils.callCustomEvent("Custom Event", this);
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
D/Retargetly -: Event : custom, value:Test Custom Event, status: 200

//Failure
D/Retargetly -: Event : custom, 500
```

## Built With

* [Android Studio](https://developer.android.com/) - Ide development

## Versioning

For the versions available, see the [tags on this repository](https://github.com/nextdots/retargetly-android-lib.git).

## Authors

* [**Luis Tundisi**](mailto:luistundisi@gmail.com) - [NextDots](http://nextdots.com/)
