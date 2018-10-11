package com.example.ding.vm;


import android.arch.lifecycle.MutableLiveData;

import com.example.commonlib.base.BaseViewModel;

import java.util.List;

public class MainViewModel extends BaseViewModel {
    private MutableLiveData<String> mShowTxt = new MutableLiveData<>();
    private MutableLiveData<String> weatherLiveData = new MutableLiveData<>();
private MutableLiveData<List<String>> listMutableLiveData=new MutableLiveData<>();
    @Override
    public void onCreate() {
        super.onCreate();
        //页面初始化的一些操作
    }

    public MutableLiveData<String> getWeatherLiveData() {
        return weatherLiveData;
    }
    /**
     * 可不写此方法
     * 使用getShowTxt.setValue(String weatherLiveData)
     *
     * @param weatherLiveData
     */
    public void setWeatherLiveData(String weatherLiveData) {
        this.weatherLiveData.setValue(weatherLiveData);
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

    public MutableLiveData<List<String>> getListMutableLiveData() {
        return listMutableLiveData;
    }

}
