package com.example.ding.base;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;

import com.example.commonlib.basebinding.rv.BaseBindingAdapter;
import com.example.commonlib.basebinding.rv.BaseBindingVH;
import com.example.commonlib.baserx.Event;
import com.example.commonlib.base.BaseActivity;
import com.example.ding.base.databinding.ActivityMainBinding;
import com.example.ding.base.BR;
import com.example.ding.vm.MainViewModel;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private BaseBindingAdapter mAdapter;

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
        mBinding.r.setLayoutManager(new LinearLayoutManager(this));
        mBinding.r.setHasFixedSize(true);
        mAdapter=new BaseBindingAdapter(this,R.layout.main_item){
            //可复写
            @Override
            public void onBindViewHolder(BaseBindingVH holder, int position) {
                super.onBindViewHolder(holder, position);//复写是不可删除
            }
        };
        mBinding.r.setAdapter(mAdapter);
    }

    @Override
    public void initViewObservable() {
        mViewModel.getShowTxt().observe(this,
                s -> Snackbar.make(mBinding.getRoot(), "editText内容" + s, Snackbar.LENGTH_SHORT).show());
        mViewModel.getListMutableLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                mAdapter.setDatas(strings);
            }
        });
    }
}
