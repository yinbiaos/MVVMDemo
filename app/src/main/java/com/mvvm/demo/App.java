package com.mvvm.demo;

import android.app.Application;
import android.os.Environment;

import com.base.lib.log.Logs;

import java.io.File;

/**
 * @author yinbiao
 * @date 2019/3/8
 */
public class App extends Application {

    public static final String PATH =
            Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID + "/log/";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
