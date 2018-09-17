package com.example.ding.base;

import com.example.commonlib.config.Global;
import com.example.commonlib.ui.BaseApp;

public class App extends BaseApp implements Thread.UncaughtExceptionHandler {
    @Override
    public void onCreate() {
        super.onCreate();
        Global.application = this;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //todo 全局try catch处理

    }
}
