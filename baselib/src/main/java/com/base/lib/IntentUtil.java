package com.base.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by biao.yin on 2018/4/25.
 * Intent跳转类，主要用于跳转到一些系统的页面
 *
 * @author yinbiao
 */

public class IntentUtil {

    private IntentUtil() {
    }

    /**
     * 跳转到指定页面
     *
     * @param context context
     * @param cls     指定页面.class
     */
    public static void startIntent(Context context, Class<?> cls) {
        startIntent(context, cls, new Bundle());
    }

    /**
     * 跳转到指定页面
     *
     * @param context context
     * @param cls     指定页面.class
     * @param bundle  参数
     */
    public static void startIntent(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 权限管理被用户拒绝后
     * 启动当前应用设置页面
     *
     * @param context     context
     * @param requestCode 请求code
     */
    public static void startIntentSettings(Activity context, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到拨号界面，不需要权限
     *
     * @param context     context
     * @param phoneNumber 号码
     */
    public static void startIntentCall(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 返回到桌面,不退出程序
     *
     * @param context context
     */
    public static void startIntentHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }
}
