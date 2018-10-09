# Android Atovi Lock SDK

## Table of contents

1. [Prerequisites](#prerequisites)
2. [Setup](#setup)
3. [Usage](#usage)
   1. [Scan Lock](#scan-lock)
        1. [callback](#scan-callback)
        2. [init](#init-scanlockapi)
        3. [start scan](#start-scan)
        4. [stop scan](#start-scan)
   2. [Control Lock](#control-lock)
        1. [callback](#control-callback)
        2. [init](#init-atovilockapi)
        3. [create connection](#create-connection)
        4. [control](#control)
        5. [change lock key](#change-lock-key)
        6. [disconnect](#disconnect)
        7. [Functions](#functions)
4. [Support](#support)
5. [Thanks](#thanks)
6. [License](#license)

## Prerequisites
### IDE
`Android Studio`
### Minimum SDK Version
`18`
### Lock Use
[Atovi Lock](https://atovi.vn)
## Setup
#### Step 1
Download [atovi-lock-api.aar](https://github.com/GiaoThoaTech/android-atovi-lock-sdk/raw/master/app/sdk/atovi-lock-sdk.aar)
#### Step 2
Copy file `atovi-lock-api.aar` you have just download and paste it in libs folder (create if not exists) under app folder of your project

#### Step 3 Config build.gradle
in build.gradle(app)
```gradle
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
    // atovi sdk
    implementation(name: 'atovi-lock-sdk', ext: 'aar')
    // lib ble
    implementation 'com.polidea.rxandroidble:rxandroidble:1.4.0'
    implementation 'com.trello:rxlifecycle:1.0'
    implementation 'com.trello:rxlifecycle-components:1.0'
    ...
}
```
## Usage
### Scan Lock
#### scan callback:
```java
private ScanLockCallback scanLockCallback = new ScanLockCallback(){
    @Override
    public void bleState(BleState bleState) {
        // hanlde it to request user allow permission, service
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
#### init scanLockAPI

```java
ScanLockAPI scanLockAPI = new ScanLockAPI(context, scanLockCallback);
```

#### start scan
```java
scanLockAPI.startScan();
```
#### stop scan
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
#### control callback
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
                // add lock and update lock
                // save it into database
            }
        }

        @Override
        public void connectStatus(BleDeviceManager.ConnectStatus status) {
            // status of ble
            // hanlde and update ui
            switch (status) {
                case CONNECTING:
                    // ble connecting to lock
                case CONNECTED:
                    // ble connected to lock
                case DISCONNECT:
                    // ble disconnected to lock
            }
        }

        @Override
        public void lockStatus(LockStatus lockStatus) {
            // status of lock hanlde and update ui
            // method will be call when connect to lock and when cotrol lock
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
            // result change key
        }

        @Override
        public void wrongKey(int result) {
            // result when try connect to lock but input wrong key
        }
    };
```
#### init AtoviLockAPI
```java
// model AtoviLock
AtoviLock atoviLock = new AtoviLock("lock address",
                "lock name",
                "key",
                boolean isAdmin); // isAdmin default is false
// AtoviLockAPI
AtoviLockAPI atoviLockAPI = new AtoviLockAPI(context, 
            atoviLock, atoviLockCallback);
```
#### create connection
```java
atoviLockAPI.connect();
```
#### control
```java
atoviLockAPI.control();
```
#### change lock key
```java
// key is type of int
atoviLockAPI.changeKey(oldKey, newKey);
```
#### disconnect
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
#### Functions

| Functions | Description | Method Callback
| --- | --- | --- |
| connect() | connect to lock | - bleState(BleState bleState) </br>- updateLock(int result, AtoviLock atoviLock) </br>- connectStatus(ConnectStatus status) </br>- lockStatus(LockStatus lockStatus) </br>- wrongKey(int result)|
| disConnect() | disconnect to lock |- bleState(BleState bleState) </br>- connectStatus(ConnectStatus status) </br>- lockStatus(LockStatus lockStatus)|
| isConnected() | check ble and lock is connected | value boolean|
| control() | action control lock (If the lock was closed then open and vice versa; If lock is disconnect then method connect() will be call firstly)|- updateLock(int result </br>- AtoviLock atoviLock) </br>- lockStatus(LockStatus lockStatus)|
| changeKey(int oldKey, int newKey) | change key access lock | changeKey(boolean isSuccess, Integer newKey) |

## Support
drop an email to tech@giaothoatech.com for more info
 
## Thanks
[Polidea RxAndroidBle](https://github.com/Polidea/RxAndroidBle)

[Trello RxLifecycle](https://github.com/trello/RxLifecycle)

## License
Copyright 2017 Atovi Smart Lock

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WaarANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.



