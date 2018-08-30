package com.example.commonlib.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.commonlib.config.Global;


public class BaseViewModel extends AndroidViewModel {
    /**
     * 网络是否可用
     */
    protected MutableLiveData<Boolean> isNetworkAvailable = new MutableLiveData<>();
    /**
     * 页面无数据
     */
    protected MutableLiveData<Boolean> isNoData = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public BaseViewModel() {
        super(Global.application);
    }

    public void onDestroy() {
        onCleared();
    }

    public void onCreate() {
        isNoData.setValue(false);
    }

    public void registerRxBus() {
    }

    public void removeRxBus() {

    }

    public MutableLiveData<Boolean> getIsNetworkAvailable() {
        return isNetworkAvailable;
    }

    public void setIsNetworkAvailable(Boolean isNetworkAvailable) {
        this.isNetworkAvailable.setValue(isNetworkAvailable);
    }

    public MutableLiveData<Boolean> getIsNoData() {
        return isNoData;
    }

    public void setIsNoData(Boolean isNoData) {
        this.isNoData.setValue(isNoData);
    }

}
