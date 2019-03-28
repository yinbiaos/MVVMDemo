package com.mvvm.demo.activity.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.lib.Logs;
import com.google.android.material.tabs.TabLayout;
import com.mvvm.demo.BaseLazyFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.PubActivity;
import com.mvvm.demo.activity.project.projectlist.ProjectListFragment;
import com.mvvm.demo.adapter.ViewPagerAdapter;
import com.mvvm.demo.entity.ProjectBean;
import com.mvvm.demo.entity.ResponseBean;

import org.apache.commons.text.StringEscapeUtils;

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
 * 项目TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class ProjectFragment extends BaseLazyFragment {

    public static final String TAG = "ProjectFragment";


    public static final int REQ_CODE = 0x11;

    @BindView(R.id.vp_pub)
    ViewPager mViewPager;
    @BindView(R.id.tab_pub)
    TabLayout mTablayout;
    @BindView(R.id.imv_more)
    ImageView mImvMore;
    ViewPagerAdapter mPubAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    private ProjectViewModel viewModel;

    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mPubAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(mPubAdapter);
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mImvMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PubActivity.class);
            intent.putExtra("title", "项目列表");
            intent.putExtra("titleList", (Serializable) titleList);
            startActivityForResult(intent, REQ_CODE);
        });
    }

    @Override
    protected void onLazyLoad() {
        Logs.d(TAG, "onLazyLoad");
        viewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);
        viewModel.getProjectList();
        viewModel.getResult().observe(this, (ResponseBean<List<ProjectBean>> result) -> {
            if (result == null) {
                return;
            }
            if (null != titleList) {
                titleList.clear();
            }
            for (ProjectBean pb : result.getData()) {
                titleList.add(StringEscapeUtils.unescapeHtml4(pb.getName()));
                fragmentList.add(ProjectListFragment.newInstance(pb.getId()));
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
