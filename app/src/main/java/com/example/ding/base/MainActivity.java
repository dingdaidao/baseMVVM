package com.example.ding.base;

import com.example.commonlib.baserx.Event;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.base.BaseViewModel;
import com.example.ding.vm.MainViewModel;

public class MainActivity extends BaseActivity {

    @Override
    protected void dealWithAction(Event event) {

    }

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.mainVM;
    }

    @Override
    public BaseViewModel initViewModel() {
        return new MainViewModel();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }
}
