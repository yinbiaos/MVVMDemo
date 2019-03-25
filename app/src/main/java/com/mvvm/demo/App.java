package com.mvvm.demo;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.base.lib.SharedHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.File;
import java.util.Stack;

/**
 * @author yinbiao
 * @date 2019/3/8
 */
public class App extends Application {

    private static App instance;

    /**
     * 日志文件存储路径
     */
    public static final String LOG_PATH = Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID + "/log/";


    /**
     * 缓存Activity栈
     */
    private Stack<Activity> stack = new Stack<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化SharedPreference
        SharedHelper.getInstance().init(getApplicationContext());
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context));
        SmartRefreshLayout.setDefaultRefreshFooterCreator(((context, layout) -> new ClassicsFooter(context)));
    }

    /**
     * 获取Application实例
     *
     * @return App
     */
    public static synchronized App getInstance() {
        return instance;
    }

    /**
     * Activity入栈
     *
     * @param context context
     */
    public void addContext(Activity context) {
        stack.push(context);
    }

    /**
     * Activity出栈
     *
     * @param context context
     */
    public void removeContext(Activity context) {
        stack.remove(context);
    }

    /**
     * 获取栈顶的Activity
     *
     * @return Activity
     */
    public Activity getTopContext() {
        return stack.peek();
    }
}
