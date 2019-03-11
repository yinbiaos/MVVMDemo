package com.mvvm.demo.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.base.lib.IntentUtil;
import com.base.lib.ToastUtil;
import com.base.lib.log.Logs;
import com.mvvm.demo.App;
import com.mvvm.demo.BaseActivity;
import com.mvvm.demo.BuildConfig;
import com.mvvm.demo.CrashHandler;
import com.mvvm.demo.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 启动页
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class SplashActivity extends BaseActivity {

    Disposable disposable;

    //需要的权限集合
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        disposable = new RxPermissions(this).requestEachCombined(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(@NonNull Permission permission) {
                if (permission.granted) {
                    //权限通过了
                    openLogSystem();
                    startIntent();
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 拒绝权限请求,弹出对话框提示用于重新授权
                    requestPermissions();
                } else {
                    // 拒绝权限请求,并不再询问,可以弹出对话框提醒用户进入设置界面去设置权限
                    ToastUtil.showToast(mContext, "已拒绝权限" + permission.name + "并不再询问");
                }
            }
        });
    }

    /**
     * 开启文件系统
     */
    private void openLogSystem() {
        //初始化全局异常捕获
        new CrashHandler().init(this.getApplicationContext(), BuildConfig.DEBUG, App.PATH + "crash.log");
        //初始化日志
        Logs.configure(BuildConfig.DEBUG, App.PATH + "log.temp");
    }

    /**
     * 跳转
     */
    private void startIntent() {
        new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                IntentUtil.startIntent(mContext, MainActivity.class);
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
