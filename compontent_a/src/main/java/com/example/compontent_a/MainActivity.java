package com.example.compontent_a;

import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.baserx.Event;
import com.example.compontent_a.databinding.ComponentAActivityMainBinding;

public class MainActivity extends BaseActivity<ComponentAActivityMainBinding, MainVM> {
    @Override
    protected void dealWithAction(Event event) {

    }

    @Override
    public int initContentView() {
        return R.layout.component_a_activity_main;
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
