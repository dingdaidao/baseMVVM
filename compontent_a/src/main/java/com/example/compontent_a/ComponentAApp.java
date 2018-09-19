package com.example.compontent_a;

import com.billy.cc.core.component.CC;
import com.example.commonlib.ui.BaseApp;

public class ComponentAApp extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableDebug(true);
        CC.enableRemoteCC(true);
    }
}
