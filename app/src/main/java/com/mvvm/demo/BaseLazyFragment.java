package com.mvvm.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logs.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logs.d(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logs.d(TAG, "onActivityCreated");
        isPrepared = true;
        onLazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLazyLoaded && isPrepared) {
            isLazyLoaded = true;
            onLazyLoad();
        }
    }

    /**
     * 懒加载数据
     */
    protected abstract void onLazyLoad();
}
