package com.mvvm.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mvvm.demo.BaseFragment;
import com.mvvm.demo.R;

import butterknife.BindView;

/**
 * 首页TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.textView)
    TextView textView;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText("aaaaaa");
    }
}
