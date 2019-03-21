package com.base.lib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by biao.yin on 2018/4/25.
 * SharedPreferences
 * 存储路径 /data/data/package_name/shared_prefs，私有目录，没root看不到
 *
 * @author yinbiao
 */

public class SharedHelper {

    private static SharedHelper instance;
    private SharedPreferences sp;

    private SharedHelper() {
    }

    /**
     * get instance 懒汉模式
     *
     * @return SharedHelper
     */
    public static SharedHelper getInstance() {
        if (instance == null) {
            synchronized (SharedHelper.class) {
                if (instance == null) {
                    instance = new SharedHelper();
                }
            }
        }
        return instance;
    }

    /**
     * init in your application
     *
     * @param context your application context
     */
    public void init(Context context) {
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void put(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void put(String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void put(String key, float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public void put(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 清除缓存
     */
    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

}
