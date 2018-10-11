package com.example.commonlib.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlib.baserx.Event;
import com.example.commonlib.baserx.RxBus;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by ding on 2017/6/15.
 */
public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {
    protected V mBinding;
    protected VM mViewModel;
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isFirstLoad = true;
    private Disposable mDispose;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 要想fragment依附于activity，请重写此方法，并返回true
     * （fragment xml根布局为，merge时）
     *
     * @return
     */
    protected boolean getAttachToParent() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.onDestroy();
        }
        mViewModel = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, initContentView(), container, getAttachToParent());
        mBinding.setVariable(initVariableId(), mViewModel = initViewModel());
        mBinding.executePendingBindings();
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDispose != null && !mDispose.isDisposed()) {
            mDispose.dispose();
            mDispose = null;
        }
        if (mViewModel != null) {
            mViewModel.onDestroy();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        initViewObservable();
        lazyLoad();
        commonLoad();
        mDispose = RxBus.getDefault().take().subscribe(new Consumer<Event>() {
            @Override
            public void accept(Event event) throws Exception {
                dealWithAction(event);
            }
        });
    }

    protected abstract void commonLoad();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onInvisible() {

    }

    private void onVisible() {
        lazyLoad();
    }

    private void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            //if (!isAdded() || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }

    protected abstract void dealWithAction(Event event);

    //刷新布局
    public void refreshLayout() {
        if (mViewModel != null) {
            mBinding.setVariable(initVariableId(), mViewModel);
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
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public abstract VM initViewModel();

    /**
     * fragment可见时才加载数据data
     */
    public abstract void initData();

    public abstract void initViewObservable();

    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }

    }
}
