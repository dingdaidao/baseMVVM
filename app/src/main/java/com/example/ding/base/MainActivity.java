package com.example.ding.base;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.example.commonlib.baserx.Event;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.base.BaseViewModel;
import com.example.ding.base.databinding.ActivityMainBinding;
import com.example.ding.vm.MainViewModel;

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

    @Override
    public void initViewObservable() {
        mViewModel.getShowTxt().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Snackbar.make(mBinding.getRoot(), "editText内容" + s, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
