package com.mvvm.demo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

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
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context));
        SmartRefreshLayout.setDefaultRefreshFooterCreator(((context, layout) -> new ClassicsFooter(context)));
    }
}
