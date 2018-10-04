package com.atovi.locksdk.example;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
