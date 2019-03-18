package com.mvvm.demo;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * @author yinbiao
 * @date 2019/3/8
 */
public class App extends Application {

    //文件路径
    public static final String LOG_PATH =
            Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID + "/log/";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
