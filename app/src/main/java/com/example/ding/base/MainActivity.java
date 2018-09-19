package com.example.ding.base;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.commonlib.baserx.Event;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.base.BaseViewModel;
import com.example.commonlib.config.Global;
import com.example.ding.base.databinding.ActivityMainBinding;
import com.example.ding.vm.MainViewModel;
import com.facebook.stetho.common.LogUtil;
import com.google.gson.Gson;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {


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
    public MainViewModel initViewModel() {
        return new MainViewModel();
    }

    @Override
    public void initData() {

    }

    IComponentCallback resultCallback = new IComponentCallback() {
        @Override
        public void onResult(CC cc, CCResult result) {
            dealWithResult(result);
        }
    };

    private void dealWithResult(CCResult result) {
        LogUtil.d(new Gson().toJson(result));
    }

    @Override
    public void initViewObservable() {
        mViewModel.getShowTxt().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Snackbar.make(mBinding.getRoot(), "editText内容" + s, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 跳转至componentA组件
     *
     * @param view
     */
    public void start2ComponentA(View view) {
        CC.obtainBuilder(Global.COMPONENTA_NAME)
                .setActionName("showActivityA")
                .build().call();
    }

    /**
     * 跳转至componentDemo组件
     *
     * @param view
     */
    public void start2ComponentDemo(View view) {
        CC.obtainBuilder(Global.COMPONENTDEMO_NAME)
                .setActionName("showDemoMain")
                .build().call();
    }
}
