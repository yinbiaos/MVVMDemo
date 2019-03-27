package com.mvvm.demo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

/**
 * Fragment页面数据加载动画基类
 *
 * @author yinbiao
 * @date 2019/3/20
 */
public abstract class BaseLoadAnimFragment extends BaseFragment implements LoadAnimProcess {

    Fragment loadPage;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPage = new LoadAnimFragment();
    }


    @Override
    public void startLoading(int layoutId) {
        if (loadPage != null && !loadPage.isAdded()) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.add(layoutId, loadPage);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void done() {
        if (loadPage != null && loadPage.isAdded()) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.remove(loadPage);
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
