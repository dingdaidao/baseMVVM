package com.example.commonlib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.commonlib.BuildConfig;
import com.example.commonlib.R;
import com.example.commonlib.config.Global;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * @author ztn
 * @version V_5.0.0
 * @date 2016年2月19日
 * @description 加载图片工具类
 */
public class LoadingImgUtil {

    public static void displayImageWithoutPlaceholder(String url, ImageView imageView, boolean isHeader) {
        if (imageView == null || imageView.getContext() == null) {
            return;
        }
        int resId = isHeader ? R.mipmap.common_default_head : R.mipmap.common_falseimg;
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(resId);
            return;
        }
        String imageUrlHost = replaceImageUrlHost(url);
//        ProgressInterceptor.addListener(imageUrlHost, progressListener);
        RequestOptions options = new RequestOptions()
                .placeholder(resId)
                .fallback(resId)
                .error(resId)
                .fitCenter()
                .skipMemoryCache(false)
                .override(960, 640)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
//        if (imageView != null) {
//            options = options.override(imageView.getWidth(), imageView.getHeight());
//        }
        Glide.with(Global.application)
                .load(imageUrlHost)
                .apply(options)
//                .override(imageSize.width, imageSize.height)
                .into(imageView);
    }


    public static void loadimg(String url, ImageView imgview, boolean flag) {
        if (imgview == null || imgview.getContext() == null)
            return;
        int resId = flag ? R.mipmap.common_default_head : R.mipmap.bg_moren;
        RequestOptions options = new RequestOptions().placeholder(resId)
                .error(resId)
                .fallback(resId)
//                .thumbnail(0.1f)
                .override(960, 640)
                .dontAnimate()
                .dontTransform()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(Global.application).load(replaceImageUrl(url))
                .apply(options)
                .listener(new LoggingListener<Drawable>())
                .into(imgview);

    }

    public static void loadimgCentrop(String url, ImageView imgview, boolean flag) {
        if (imgview == null || imgview.getContext() == null)
            return;
        int resId = flag ? R.mipmap.common_default_head : R.mipmap.bg_moren;
        RequestOptions options = new RequestOptions().placeholder(resId)
                .error(resId)
                .fallback(resId)
                .centerCrop()
//                .thumbnail(0.1f)
                .override(960, 640)
                .dontAnimate()
                .dontTransform()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(Global.application).load(replaceImageUrl(url))
                .apply(options)
                .listener(new LoggingListener<Drawable>())
                .into(imgview);

    }

    public static void loadingLocalImage(String url, ImageView imageView) {
        if (imageView == null || imageView.getContext() == null)
            return;
        RequestOptions options = new RequestOptions().dontAnimate()
                .dontTransform().diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(imageView.getWidth(), imageView.getHeight());
        Glide.with(Global.application)
                .load(url)
                .apply(options).listener(new LoggingListener<Drawable>())
                .into(imageView);
    }

    public static void getCacheImage(Context context, String path, final Handler handler, final onLoadingImageListener listener) {
       /* Glide.with(Global.application).load(path).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (listener != null) {
                            listener.onLoadingComplete(resource);
                        }
                    }
                });*/
    }

   /* public static void getImageFile(String path, final OnFileImageLoadingListener listener) {
        Glide.with(Global.application).load(replaceImageUrlHost(path)).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                if (listener != null) {
                    listener.onFileLoadingComplete(resource);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (listener != null) {
                    listener.onLoadingFailed();
                }
            }
        });
    }*/

    public static String replaceImageUrlHost(String url) {
        String httpUrl = url;
        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            httpUrl = Global.baseURL + url;
        }
        return httpUrl;
    }

    public static void resumeLoading() {
        Glide.with(Global.application).resumeRequests();
    }

    public static void pauseLoading() {
        Glide.with(Global.application).pauseRequests();
    }

    public interface onLoadingImageListener {
        void onLoadingComplete(Bitmap bitmap);

        void onLoadingFailed();
    }

    public interface OnFileImageLoadingListener extends onLoadingImageListener {
        void onFileLoadingComplete(File file);
    }

    public static class LoggingListener<T> implements RequestListener<T> {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<T> target, boolean isFirstResource) {
            if (BuildConfig.DEBUG) {
                Logger.d("onException", e);
            }
            return false;
        }

        @Override
        public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }

    }

    public static String replaceImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        //从e学迁移过来的图片不是完整的地址，需要拼接https://m.imexue.com
        if (!url.startsWith("http") && (url.startsWith("/data/psmg/") || url.startsWith("/data/pimgs/"))) {
            return "https://m.imexue.com" + url.replace("/psmg/", "/pimgs/");
        }
        return url.replace("/test.img.juziwl.cn/", "/test.view-img.juziwl.cn/")
                .replace("/dfs.img.jzexueyun.com/", "/dfs.view-img.jzexueyun.com/");
    }


}
