package com.example.commonlib.basebinding.viewadapter.image;


import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlib.util.LoadingImgUtil;


/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    /**
     * 加载远程服务器图片
     *
     * @param imageView      ImageView
     * @param url            String url地址
     * @param placeholderRes 占位图
     */
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            LoadingImgUtil.loadimg(url, imageView, false);
        } else {
//            imageView.setImageResource(R.mipmap.bg_moren);
            //TODO
        }
    }

    /**
     * @param imageView
     * @param url
     * @param placeholderRes
     */
    @BindingAdapter(value = {"headurl", "placeholderRes"}, requireAll = false)
    public static void setHeadImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url))
            //使用Glide框架加载图片
            LoadingImgUtil.loadimg(url, imageView, true);
        else {
//            imageView.setImageResource(R.mipmap.common_default_head);
        }
    }

    /**
     * 加载本地backgroundRes
     * * @param textView
     *
     * @param res
     */
    @BindingAdapter(value = {"tvbackground"}, requireAll = false)
    public static void setTvBackground(TextView textView, int res) {
        if (res != 0) {
            textView.setBackgroundResource(res);
        }
    }

    /**
     * 加载本地resource
     *
     * @param imageView
     * @param res
     */
    @BindingAdapter(value = {"res"}, requireAll = false)
    public static void setImageUri(ImageView imageView, int res) {
        if (res != 0) {
            imageView.setImageResource(res);
        }
    }

}

