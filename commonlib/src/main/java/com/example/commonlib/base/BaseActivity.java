package com.example.commonlib.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.commonlib.R;
import com.example.commonlib.baserx.Event;
import com.example.commonlib.baserx.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity
 * {RxAppCompatActivity}(需要RxLifecycle请继承RxAppCompatActivity,方便LifecycleProvider管理生命周期)
 */

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {
    protected V mBinding;
    protected VM mViewModel;
    private MyNetBroadCastReciver receiver;
    private Disposable mDispose;

    protected AlertDialog mLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化号mviewmodel
        initViewDataBinding();
        initData();
        initViewObservable();
        mViewModel.onCreate();
        mViewModel.registerRxBus();
        mDispose = RxBus.getDefault().take().subscribe(new Consumer<Event>() {
            @Override
            public void accept(Event event) throws Exception {
                dealWithAction(event);
            }
        });
        setNetworkBroadcast();

    }

    public boolean needBackTimer() {
        return false;
    }

    protected abstract void dealWithAction(Event event);

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null)
            unregisterReceiver(receiver);
        mViewModel.removeRxBus();
        mViewModel.onDestroy();
        mViewModel = null;

        if (mDispose != null && !mDispose.isDisposed())
            mDispose.dispose();
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        mBinding = DataBindingUtil.setContentView(this, initContentView());
        mBinding.setVariable(initVariableId(), mViewModel = initViewModel());
        mBinding.executePendingBindings();
        //liveData绑定activity，fragment生命周期
        mBinding.setLifecycleOwner(this);
    }

    //刷新布局
    public void refreshLayout() {
        if (mViewModel != null) {
            mBinding.setVariable(initVariableId(), mViewModel);
            mBinding.executePendingBindings();
        }
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView();

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id(根据对应xml文件 < data > < variable > name < / variable > < / data > 自动生成)
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public abstract VM initViewModel();

    public abstract void initData();

    /**
     * 处理livedata的订阅方法
     */
    public abstract void initViewObservable();

    /**
     * 设置网络监听
     */
    private void setNetworkBroadcast() {
        receiver = new MyNetBroadCastReciver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    /**
     * ==================================判断网络连接======================================
     */
    class MyNetBroadCastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是在开启wifi连接和有网络状态下
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (NetworkInfo.State.CONNECTED == info.getState()) {
                    //连接状态 处理自己的业务逻辑
                    mViewModel.setIsNetworkAvailable(true);
                } else {
//                    Toast.makeText(context, "网络链接失败", Toast.LENGTH_SHORT).show();
                    mViewModel.setIsNetworkAvailable(true);
                }
            }
        }

    }

    public void showLoadingDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_loading_dialog, null);
        AVLoadingIndicatorView avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.AVLoadingIndicatorView);
        mLoadingDialog = new AlertDialog.Builder(this, R.style.Dialog).setCancelable(false).create();
        mLoadingDialog.setView(view);
        Window window = mLoadingDialog.getWindow();
        if (mLoadingDialog != null && window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
//                attr.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            }
        }
        mLoadingDialog.show();
        avLoadingIndicatorView.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }
}
