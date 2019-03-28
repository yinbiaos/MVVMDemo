package com.mvvm.demo.activity.system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.mvvm.demo.BaseLazyFragment;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
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

    public static SystemFragment newInstance() {
        return new SystemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_system, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mPubAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(mPubAdapter);
        mImvMore.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PubActivity.class);
            intent.putExtra("title", "体系列表");
            intent.putExtra("titleList", (Serializable) titleList);
            startActivityForResult(intent, REQ_CODE);
        });
    }

    @Override
    protected void onLazyLoad() {
        viewModel = ViewModelProviders.of(this).get(SystemViewModel.class);
        viewModel.getData();
        viewModel.getResult().observe(this, (ResponseBean<List<SystemDataBean>> result) -> {
            if (result == null) {
                return;
            }
            List<SystemDataBean> list = result.getData();
            titleList.clear();
            for (SystemDataBean sdb : list) {
                titleList.add(sdb.getName());
                fragmentList.add(SubSystemFragment.newInstance(sdb.getChildren()));
            }
            mPubAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = data.getIntExtra("position", 0);
        mViewPager.setCurrentItem(position);
    }

}
