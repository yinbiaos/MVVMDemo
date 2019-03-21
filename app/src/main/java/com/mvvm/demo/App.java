package com.mvvm.demo;

import android.app.Application;
import android.os.Environment;

import com.base.lib.SharedHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.File;

/**
 * @author yinbiao
 * @date 2019/3/8
 */
public class App extends Application {

    /**
     * 日志文件存储路径
     */
    public static final String LOG_PATH = Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID + "/log/";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化SharedPreference
        SharedHelper.getInstance().init(getApplicationContext());
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context));
        SmartRefreshLayout.setDefaultRefreshFooterCreator(((context, layout) -> new ClassicsFooter(context)));
    }
}
