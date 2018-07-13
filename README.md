![Retargetly](https://github.com/retargetly/sdk-ios/blob/master/rely_artboard.png)

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
    jcenter { url "http://jcenter.bintray.com/" }
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency

```gradle
dependencies {
  implementation 'com.github.retargetly:sdk-android:2.0.2'
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
