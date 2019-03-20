package com.mvvm.demo.http;

import android.support.annotation.NonNull;

import com.base.lib.Logs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by biao.yin on 2018/4/26.
 * 网络请求拦截器，进行日志输出
 * 使用方式new OkHttpClient.Builder().addInterceptor(new HttpLogInterceptor())
 *
 * @author yinbiao
 */

public class HttpLogInterceptor implements Interceptor {

    private static final String TAG = "HttpLogInterceptor";

    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        //请求发起的时间
        long t1 = System.nanoTime();
        Logs.i(TAG, String.format(Locale.getDefault(), "发送请求: %s  %nmethod: %s   ", request.url(), request.method()));
        RequestBody requestBody = request.body();
        if (requestBody != null && requestBody.contentLength() > 0) {
            StringBuilder sb = new StringBuilder();
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            sb.append(buffer.readString(Charset.defaultCharset()));
            Logs.i(TAG, String.format(Locale.getDefault(), "请求参数 %s", sb.toString()));
        }

        Response response = chain.proceed(request);
        //收到响应的时间
        long t2 = System.nanoTime();
        //不能直接使用response.body（）.string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，
        // 我们需要创建出一个新的response给应用层处理
        ResponseBody body = response.body();
        if (body != null) {
            //这里body.contentLength()有可能为-1，需要特殊处理
            ResponseBody rb = response.peekBody(body.contentLength() > 0 ? body.contentLength() : Integer.MAX_VALUE);
            Logs.i(TAG, String.format(Locale.getDefault(), "耗时：%.1fms 接收响应：%s  %n响应码：[%s] %n返回json:%s ",
                    (t2 - t1) / 1e6d,
                    response.request().url(),
                    response.code(),
                    rb.string()));
        }
        if (!response.isSuccessful()) {
            Logs.d(TAG, "HttpException:" + response.code() + "：" + response.message());
//            throw new IOException(response.message());
        }
        return response;
    }
}
