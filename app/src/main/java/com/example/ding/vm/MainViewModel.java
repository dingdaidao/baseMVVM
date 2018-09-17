package com.example.ding.vm;


import android.arch.lifecycle.MutableLiveData;

import com.example.commonlib.base.BaseViewModel;

public class MainViewModel extends BaseViewModel {
    private MutableLiveData<String> mShowTxt = new MutableLiveData<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MutableLiveData<String> getShowTxt() {
        return mShowTxt;
    }

    /**
     * 可不写此方法
     * 使用getShowTxt.setValue(String showTxt)
     *
     * @param showTxt
     */
    public void setShowTxt(String showTxt) {
        mShowTxt.setValue(showTxt);
    }
}
