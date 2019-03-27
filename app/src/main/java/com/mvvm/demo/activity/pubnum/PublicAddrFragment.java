package com.mvvm.demo.activity.pubnum;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mvvm.demo.BaseLoadAnimFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.PubActivity;
import com.mvvm.demo.activity.pubnum.publist.PubListFragment;
import com.mvvm.demo.adapter.ViewPagerAdapter;
import com.mvvm.demo.entity.PublicAddrBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hzy on 2019/1/22
 *
 * @author hzy
 */
public class PublicAddrFragment extends BaseLoadAnimFragment {

    public static final String TAG = "PublicAddrFragment";
    public static final int REQ_CODE = 0x14;
    private static PublicAddrFragment instance = null;

    @BindView(R.id.vp_pub)
    ViewPager mViewPager;
    @BindView(R.id.tab_pub)
    TabLayout mTablayout;
    @BindView(R.id.imv_more)
    ImageView mImvMore;
    ViewPagerAdapter mPubAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    List<PublicAddrBean> list = new ArrayList<>();
    private Map<Integer, Fragment> map = new HashMap<>();

    private PubViewModel viewModel;

    public static PublicAddrFragment getInstance() {
        if (instance == null) {
            instance = new PublicAddrFragment();
        }
        return instance;
    }

    public static PublicAddrFragment getInstance(String title) {
        if (instance == null) {
            Bundle bundle = new Bundle();
            bundle.putString(TAG, title);
            instance = new PublicAddrFragment();
            instance.setArguments(bundle);
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pub, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
    }

    protected void initEventAndData() {
        mImvMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PubActivity.class);
            intent.putExtra("title", "公众号列表");
            intent.putExtra("titleList", (Serializable) titleList);
            getActivity().startActivityForResult(intent, REQ_CODE);
        });
        viewModel = ViewModelProviders.of(this).get(PubViewModel.class);
        viewModel.getData();
        viewModel.getResult().observe(this, result -> {
            List<PublicAddrBean> list = result.getData();
            if (result == null) {
                return;
            }
            updateView(list);
        });

    }

    public int getAddrId() {
        int id = 408;
        id = list.get(mViewPager.getCurrentItem()).getId();
        return id;
    }

    public void updateView(List<PublicAddrBean> list) {
        this.list = list;
        if (null != titleList) {
            titleList.clear();
        }
        fragmentList = new ArrayList<>();
        for (PublicAddrBean bean : list) {
            Fragment fragment = null;
            int id = bean.getId();
            if (map.containsKey(id)) {
                fragmentList.add(map.get(id));
            } else {
                fragment = PubListFragment.newInstance(id);
                fragmentList.add(fragment);
            }
            titleList.add(bean.getName());
            if (fragment != null) {
                map.put(id, fragment);
            }
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
