package com.example.demo;

import com.billy.cc.core.component.CC;
import com.example.commonlib.ui.BaseApp;

public class ComponentDemoApp extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableRemoteCC(true);
    }
}
