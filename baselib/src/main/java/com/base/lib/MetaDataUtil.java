package com.base.lib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by biao.yin on 2018/4/26.
 * 获取AndroidMainifest中配置的数据
 *
 * @author yinbiao
 */

public class MetaDataUtil {

    /**
     * 获取Application中的metaData数据
     *
     * @param context context
     * @param key     key
     * @return String
     */
    public static String getApplicationMetaData(Context context, String key) {
        String value = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean getApplicationMetaDataBoolean(Context context, String key) {
        boolean value = false;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getBoolean(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取Activity中metaData数据
     *
     * @param context context
     * @param key     key
     * @return String
     */
    public static String getActivityMetaData(Activity context, String key) {
        String value = null;
        try {
            ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(context.getComponentName(), PackageManager.GET_META_DATA);
            value = activityInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }
}
