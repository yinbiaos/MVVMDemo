package com.mvvm.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mContext;
    protected Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View注解框架绑定
        unbinder = ButterKnife.bind(this, view);
        BarUtils.setStatusBarVisibility(getActivity(), true);
        BarUtils.setStatusBarColor(getActivity(), getResources().getColor(R.color.c_6c8cff), 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
