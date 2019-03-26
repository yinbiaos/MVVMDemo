package com.mvvm.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.base.lib.Logs;

/**
 * 懒加载Fragment
 *
 * @author yinbiao
 * @date 2019/3/25
 */
public abstract class BaseLazyFragment extends BaseLoadAnimFragment {

    private static final String TAG = "BaseLazyFragment";

    /**
     * 是否已经准备好
     */
    private boolean isPrepared = false;
    /**
     * 是否已经执行过加载
     */
    private boolean isLazyLoaded = false;
    /**
     * 是否可见的
     */
    private boolean isVisible = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化时加载数据，一般只有第一个显示的页面进入此方法
        isPrepared = true;
        if (isVisible && !isLazyLoaded) {
            isLazyLoaded = true;
            onLazyLoad();
            Logs.d(TAG, this.getClass().getSimpleName() + "onActivityCreated.onLazyLoad()");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        //切换可见时加载数据，一般除了第一个页面都进入此方法
        if (isVisible && !isLazyLoaded && isPrepared) {
            isLazyLoaded = true;
            onLazyLoad();
            Logs.d(TAG, this.toString() + "setUserVisibleHint.onLazyLoad()");
        }
    }

    /**
     * 懒加载数据
     */
    protected abstract void onLazyLoad();
}
