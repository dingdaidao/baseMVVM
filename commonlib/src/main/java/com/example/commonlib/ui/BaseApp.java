package com.example.commonlib.ui;

import android.app.Application;


public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Logger.init("bangbangtong").logLevel(LogLevel.FULL).hideThreadInfo().methodCount(0);
//        Stetho.initializeWithDefaults(this);
    }

}
