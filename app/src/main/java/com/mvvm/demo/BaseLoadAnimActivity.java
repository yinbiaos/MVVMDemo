package com.mvvm.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Activity页面数据加载动画基类
 *
 * @author yinbiao
 * @date 2019/3/20
 */
public abstract class BaseLoadAnimActivity extends BaseActivity implements LoadAnimProcess {

    Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new LoadAnimFragment();
    }

    @Override
    public void startLoading(int layoutId) {
        if (fragment != null && !fragment.isAdded()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(layoutId, fragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void done() {
        if (fragment != null && fragment.isAdded()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void error() {
        //TODO 加载错误页面
    }

    @Override
    public void doneEmpty() {
        //TODO 加载空数据页面
    }

}
