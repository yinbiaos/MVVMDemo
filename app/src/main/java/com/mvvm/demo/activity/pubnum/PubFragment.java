package com.mvvm.demo.activity.pubnum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvvm.demo.BaseFragment;
import com.mvvm.demo.R;

/**
 * 公众号TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class PubFragment extends BaseFragment {

    public static Fragment newInstance() {
        return new PubFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
