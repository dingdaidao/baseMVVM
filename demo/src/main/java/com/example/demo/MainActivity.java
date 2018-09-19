package com.example.demo;


import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.baserx.Event;
import com.example.demo.databinding.DemoActivityMainBinding;

public class MainActivity extends BaseActivity<DemoActivityMainBinding, MainVM> {
    @Override
    protected void dealWithAction(Event event) {

    }

    @Override
    public int initContentView() {
        return R.layout.demo_activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.mainVM;
    }

    @Override
    public MainVM initViewModel() {
        return new MainVM();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {
        mBinding.getRoot().setOnClickListener(v -> finish());
    }
}
