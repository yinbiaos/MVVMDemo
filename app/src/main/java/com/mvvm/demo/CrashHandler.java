package com.mvvm.demo;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.base.lib.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by biao.yin on 2018/4/25.
 * 奔溃异常处理
 * 注意需要配置文件读写权限
 *
 * @author yinbiao
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private Map<String, Object> infos = new LinkedHashMap<>();
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private boolean isDebug = false;
    private Context context;
    // 存储路径
    private String path;

    /**
     * 初始化，需要先赋予文件读写权限
     *
     * @param context context
     * @param isDebug 是否开启调试，true 打印日志，false不打印
     * @param path    日志文件的完整路径，包含文件名
     */
    public void init(Context context, boolean isDebug, String path) {
        this.context = context;
        this.isDebug = isDebug;
        this.path = path;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex Throwable
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        if (!isDebug) {
            return false;
        }
//        //使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(context, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();
        //收集设备参数信息
        collectDeviceInfo();
        //保存日志文件
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveCrashInfo2File(ex);
        }
        return true;
    }

    /**
     * 收集设备参数信息
     */
    private void collectDeviceInfo() {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        infos.put("\n\ncrashTime", time);
        infos.put("versionName", context.getPackageName());
        infos.put("versionCode", BuildConfig.VERSION_CODE);
        infos.put("sdk_int", Build.VERSION.SDK_INT);
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex Throwable
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : infos.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key).append("=").append(value.toString()).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(path, true);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
