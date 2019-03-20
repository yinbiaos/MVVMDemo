package com.mvvm.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected Context mContext;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mContext = this;
        //View注解框架绑定
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 设置ContetView的布局
     *
     * @return int
     */
    protected abstract int getContentView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            //ButterKnife解绑
            unbinder.unbind();
        }
    }

}
