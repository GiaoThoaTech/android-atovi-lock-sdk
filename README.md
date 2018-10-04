# Android Atovi Lock API
## Prerequisites
### IDE
`Android Studio`
### Minimum SDK Version
`18`
### Lock Use
`Atovi Lock`
## Installing
#### Step 1
[Download atovi-lock-api.arr](https://gitlab.com/gtek/atovi/atovi_lib_android/blob/develop/libs/atovi-lock-api.aar)
#### Step 2
Copy file `atovi-lock-api.arr` you have just download and paste it in libs folder (create if not have) under app folder of your project

#### Step 3 Config build.gradle
in build.gralde(app)
```xml
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    repositories {
        flatDir {
            dirs 'libs'
        }
    }
    ...
}

dependencies {
    ...
    //atovi sdk
    implementation(name: 'atovi-lock-sdk', ext: 'aar')
    //lib ble
    implementation 'com.polidea.rxandroidble:rxandroidble:1.4.0'
    implementation 'com.trello:rxlifecycle:1.0'
    implementation 'com.trello:rxlifecycle-components:1.0'
    ...
}
```
## Usage
### Scan Lock
callback:
```java
private ScanLockCallback scanLockCallback = new ScanLockCallback(){
        @Override
        public void bleState(BleState bleState) {
            //hanlde it to request user allow permission, service
            switch (bleState) {
                case READY:
                // everything should work
                case BLUETOOTH_NOT_ENABLED:
                 // scanning and connecting will not work
                case BLUETOOTH_NOT_AVAILABLE:
                // basically no functionality will work here
                case LOCATION_SERVICES_NOT_ENABLED:
                // scanning will not work
                case LOCATION_PERMISSION_NOT_GRANTED:
                // scanning and connecting will not work
            }
        }

        @Override
        public void startScan() {
            //function will be call when scanLockAPI.startScan()
        }

        @Override
        public void stopScan() {
             //function will be call when scanLockAPI.stopScan()
        }

        @Override
        public void foundLock(ScanLockAPI.ScanLockModel scanLockModel) {
            //found a lock
        }
    };
```
init ScanLockAPI

```java
    ScanLockAPI scanLockAPI = new ScanLockAPI(context, scanLockCallback);
```

start
```java
     scanLockAPI.startScan();
```
stop
```java
    scanLockAPI.stopScan();
```
>*Important*- need call stopScan() inside onPause() method to disable ble or otherwise it keep the scan even when close app
```java
    @Override
    protected void onPause() {
        super.onPause();
        scanLockAPI.stopScan();
    }
```

### Control Lock
callback
```java
private AtoviLockCallback atoviLockCallback = new AtoviLockCallback() {
        @Override
        public void bleState(BleState bleState) {
            //hanlde it to request user allow permission, service
            switch (bleState) {
                case READY:
                // everything should work
                case BLUETOOTH_NOT_ENABLED:
                 // scanning and connecting will not work
                case BLUETOOTH_NOT_AVAILABLE:
                // basically no functionality will work here
                case LOCATION_SERVICES_NOT_ENABLED:
                // scanning will not work
                case LOCATION_PERMISSION_NOT_GRANTED:
                // scanning and connecting will not work
            }
        }

        @Override
        public void updateLock(int result, AtoviLock atoviLock) {
            if (atoviLock != null) {
                //add lock and update lock
                //save it into data base
            }
        }

        @Override
        public void connectStatus(BleDeviceManager.ConnectStatus status) {
            //status of ble
            //hanlde and update ui
            switch (status) {
                case CONNECTING:
                    //ble connecting to lock
                case CONNECTED:
                    //ble connected to lock
                case DISCONNECT:
                    //ble disconnected to lock
            }
        }

        @Override
        public void lockStatus(LockStatus lockStatus) {
            /*status of lock
            hanlde and update ui*/
            /* method will be call when connect to lock and
            when cotrol lock*/
            switch (lockStatus) {
                case CONNECTING:
                case DISCONNECT:
                case LOCKING:
                case LOCK:
                case UNLOCKING:
                case UNLOCK:
            }
        }

        @Override
        public void changeKey(boolean isSuccess, Integer newKey) {
            //result change key
        }

        @Override
        public void wrongKey(int result) {
            //result when try connect to lock but input wrong key
        }
    };
```
Init Model AtoviLock and AtoviLockAPI
```java
AtoviLock atoviLock = new AtoviLock("lock address",
                "lock name",
                "key",
                boolean isAdmin);//can enter default is false

AtoviLockAPI atoviLockAPI = new AtoviLockAPI(context, 
            atoviLock, atoviLockCallback);
```
Open Connect
```java
atoviLockAPI.connect();
```
Control lock
```java
atoviLockAPI.control();
```
Change key
```java
//key is type of int
atoviLockAPI.changeKey(oldKey, newKey);
```
Disconnect
```java
atoviLockAPI.disConnect();
```
>*Important*- need call disConnect() inside onPause() method to disable ble or otherwise it keep the connect even when close app
```java
    @Override
    protected void onPause() {
        super.onPause();
        atoviLockAPI.disConnect();
    }
```
Function

| Function | Description | Method Callback
| --- | --- | --- |
| connect() | connect to lock | bleState(BleState bleState), updateLock(int result, AtoviLock atoviLock), connectStatus(ConnectStatus status), lockStatus(LockStatus lockStatus), wrongKey(int result) (if input wrong key)|
| disConnect() | disconnect to lock | bleState(BleState bleState),  connectStatus(ConnectStatus status), lockStatus(LockStatus lockStatus)|
| isConnected() | check ble and lock is connected | value boolean|
| control() | action control lock (If the lock was closed then open and vice versa; If lock is disconnect then method connect() will be call firstly)| updateLock(int result, AtoviLock atoviLock), lockStatus(LockStatus lockStatus)|
| changeKey(int oldKey, int newKey) | change key access lock | changeKey(boolean isSuccess, Integer newKey) |
