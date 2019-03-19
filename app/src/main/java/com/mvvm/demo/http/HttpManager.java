package com.mvvm.demo.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * HttpManager 管理类
 *
 * @author yinbiao
 * @date 2019/3/18
 */
public class HttpManager {

    public static final String BASE_URL = "http://www.wanandroid.com";

    /**
     * HttpManager 实例
     */
    private static HttpManager instance = new HttpManager();

    private Retrofit retrofit;

    /**
     * 缓存Service
     */
    private Map<Class, Object> map = new HashMap<>();

    private HttpManager() {
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new HeadInterceptor())
                .addInterceptor(new HttpLogInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    /**
     * 饿汉模式，获取HttpManager实例
     *
     * @return HttpManager
     */
    public static HttpManager getInstance() {
        return instance;
    }


    /**
     * 获取接口服务
     *
     * @param cls cls
     * @return T
     */
    public <T> T getService(Class<T> cls) {
        if (map.containsKey(cls)) {
            Object obj = map.get(cls);
            return (T) obj;
        } else {
            T t = retrofit.create(cls);
            map.put(cls, t);
            return t;
        }
    }

}
