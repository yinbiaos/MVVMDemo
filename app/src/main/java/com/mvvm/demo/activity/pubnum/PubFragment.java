package com.mvvm.demo.activity.pubnum;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvvm.demo.BaseLazyFragment;
import com.mvvm.demo.R;

/**
 * 公众号TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class PubFragment extends BaseLazyFragment {

    public static Fragment newInstance() {
        return new PubFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void onLazyLoad() {

    }
}
