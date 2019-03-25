package com.mvvm.demo.http;

import android.support.annotation.NonNull;

import com.base.lib.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.internal.annotations.EverythingIsNonNull;
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

    private static final String TAG = "HttpManager";

    public static final String BASE_URL = "https://www.wanandroid.com";

    /**
     * HttpManager 实例
     */
    private static HttpManager instance = new HttpManager();

    private Retrofit retrofit;

    /**
     * 缓存Service
     */
    private Map<Class, Object> map = new HashMap<>();

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private HttpManager() {
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new HeadInterceptor())
                .addInterceptor(new HttpLogInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cookieJar(new CookieJar() {
                    @Override
                    @EverythingIsNonNull
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        Logs.d(TAG, url.host());
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    @EverythingIsNonNull
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        Logs.d(TAG, url.host());
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<>();
                    }
                })
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
