package com.mvvm.demo.activity.project;

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

import com.mvvm.demo.BaseLazyFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.PubActivity;
import com.mvvm.demo.activity.project.projectlist.ProjectListFragment;
import com.mvvm.demo.adapter.ViewPagerAdapter;
import com.mvvm.demo.entity.ProjectBean;
import com.mvvm.demo.entity.ResponseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 项目TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class ProjectFragment extends BaseLazyFragment {

    public static final String TAG = "ProjectFragment";

    private static ProjectFragment instance = null;

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

    public static ProjectFragment getInstance() {
        if (instance == null) {
            instance = new ProjectFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onLazyLoad() {
        initEventAndData();
    }

    private void initEventAndData() {
        mImvMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PubActivity.class);
            intent.putExtra("title", "项目列表");
            intent.putExtra("titleList", (Serializable) titleList);
            getActivity().startActivityForResult(intent, REQ_CODE);
        });
//        startLoading(R.id.vp_pub);
        viewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);
        viewModel.getProjectList();
        viewModel.getResult().observe(this, (ResponseBean<List<ProjectBean>> result) -> {
//            done();
            List<ProjectBean> projectList = result.getData();
            if (result == null) {
                return;
            }
            if (null != titleList) {
                titleList.clear();
            }
            for (ProjectBean pb : projectList) {
                Fragment fragment = null;
                titleList.add(pb.getName());
                fragment = ProjectListFragment.newInstance(pb.getId());
                fragmentList.add(fragment);
            }
            mTablayout.setupWithViewPager(mViewPager);
            mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mPubAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
            mViewPager.setAdapter(mPubAdapter);
            mViewPager.setOffscreenPageLimit(5);
            mViewPager.setCurrentItem(0);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = data.getIntExtra("position", 0);
        mViewPager.setCurrentItem(position);
    }

}
