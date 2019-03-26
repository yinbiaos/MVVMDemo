package com.mvvm.demo.activity.system;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.mvvm.demo.BaseLazyFragment;
import com.mvvm.demo.BaseLoadAnimFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.PubActivity;
import com.mvvm.demo.activity.system.subsystem.SubSystemFragment;
import com.mvvm.demo.adapter.ViewPagerAdapter;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.entity.SystemDataBean;
import com.mvvm.demo.widget.TitleBarLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzy on 2019/1/22
 * 体系tab
 *
 * @author Administrator
 */
public class SystemFragment extends BaseLazyFragment {

    public static final String TAG = "SystemFragment";
    public static final int REQ_CODE = 0x12;
    private static SystemFragment instance = null;

    @BindView(R.id.title_bar)
    TitleBarLayout mTitleBar;

    @BindView(R.id.vp_pub)
    ViewPager mViewPager;
    @BindView(R.id.tab_pub)
    TabLayout mTablayout;
    @BindView(R.id.imv_more)
    ImageView mImvMore;
    ViewPagerAdapter mPubAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    private SystemViewModel viewModel;

    public static SystemFragment getInstance() {
        if (instance == null) {
            instance = new SystemFragment();
        }
        return instance;
    }

    public static SystemFragment getInstance(String title) {
        if (instance == null) {
            Bundle bundle = new Bundle();
            bundle.putString(TAG, title);
            instance = new SystemFragment();
            instance.setArguments(bundle);
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_system, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onLazyLoad() {
        initEventAndData();
    }

    protected void initEventAndData() {
        BarUtils.setStatusBarVisibility(getActivity(), true);
        BarUtils.setStatusBarColor(getActivity(), getResources().getColor(R.color.c_6c8cff), 1);

        mImvMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PubActivity.class);
            intent.putExtra("title", "体系列表");
            intent.putExtra("titleList", (Serializable) titleList);
            getActivity().startActivityForResult(intent, REQ_CODE);
        });

        viewModel = ViewModelProviders.of(this).get(SystemViewModel.class);
        viewModel.getData();
        viewModel.getResult().observe(this, (ResponseBean<List<SystemDataBean>> result) -> {
            List<SystemDataBean> list = result.getData();
            if (result == null) {
                return;
            }
            updateView(list);
        });
    }

    public void updateView(List<SystemDataBean> list) {
        if (null != titleList) {
            titleList.clear();
        }
        for (SystemDataBean sdb : list) {
            Fragment fragment = null;
            titleList.add(sdb.getName());
            fragment = SubSystemFragment.newInstance(sdb.getChildren());
            fragmentList.add(fragment);
        }
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mPubAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(mPubAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = data.getIntExtra("position", 0);
        mViewPager.setCurrentItem(position);
    }

}
