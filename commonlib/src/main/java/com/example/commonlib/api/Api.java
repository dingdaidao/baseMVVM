package com.example.commonlib.api;

import android.text.TextUtils;

import com.example.commonlib.config.Global;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class Api {
    private static final long DEFAULT_TIMEOUT = 30;
    private volatile static Api api;
    private final OkHttpClient client;
    private Retrofit retrofit;
    private Retrofit retrofit2;

    private Api() {
        //token拦截器
        TokenInterceptor tokenInterceptor = new TokenInterceptor();
        //日志拦截器
        HttpLoggingInterceptor ipadloggingInterceptor = new HttpLoggingInterceptor();
        ipadloggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
//        int size = 1024 * 1024 * 100;
//        File cacheFile = new File(BaseApp.getContext().getCacheDir(), "OkHttpCache");
//        Cache cache = new Cache(cacheFile, size);
        client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new NetworkInterceptor())
                .addInterceptor(ipadloggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Global.urlApi)
                .client(client)
                //然后将下面的GsonConverterFactory.create()替换成我们自定义的ResponseConverterFactory.create()
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Api getInstance() {
        if (api == null) {
            synchronized (Api.class) {
                if (api == null) {
                    api = new Api();
                }
            }
        }
        return api;
    }

    //   更具类型获取不同的service
    public <T> T getApiService(Class<T> tClass) {
        return retrofit.create(tClass);

    }   //   更具类型获取不同的service

    public <T> T getApi2Service(String baseUrl, Class<T> tClass) {
        retrofit2 = new Retrofit.Builder()
                .client(client)
                .baseUrl(TextUtils.isEmpty(baseUrl) ? Global.urlApi : baseUrl)
                //然后将下面的GsonConverterFactory.create()替换成我们自定义的ResponseConverterFactory.create()
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit2.create(tClass);

    }

}