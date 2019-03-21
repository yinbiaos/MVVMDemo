package com.mvvm.demo.activity.project.projectlist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.lib.ToastUtil;
import com.mvvm.demo.BaseLoadAnimFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.X5WebView;
import com.mvvm.demo.adapter.ProjectListAdapter;
import com.mvvm.demo.entity.ProjectListBean;
import com.mvvm.demo.entity.ResponseBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzy on 2019/2/20
 * 项目列表
 *
 * @author hzy
 */

public class ProjectListFragment extends BaseLoadAnimFragment {

    private static final String TAG = "ProjectListFragment";

    @BindView(R.id.rv_list)
    RecyclerView mRvProject;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    int pageIndex = 1;
    int cid = 0;
    ProjectListAdapter mAdapter;
    List<ProjectListBean.DatasBean> projectList = new ArrayList<>();

    private ProjectListViewModel viewModel;

    public static ProjectListFragment newInstance(int cid) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, cid);
        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
    }

    protected void initEventAndData() {
        viewModel = ViewModelProviders.of(this).get(ProjectListViewModel.class);

        cid = getArguments().getInt(TAG);

        mAdapter = new ProjectListAdapter(getActivity(), projectList);
        mRvProject.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvProject.setAdapter(mAdapter);
        //禁用滑动事件
        mRvProject.setNestedScrollingEnabled(false);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), X5WebView.class);
                intent.putExtra("mUrl", projectList.get(position).getLink());
                intent.putExtra("mTitle", projectList.get(position).getTitle());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
                return false;
            }
        });
        mAdapter.setmOnCollectListener((collect, id, position) -> {
//            startLoading(R.id.content);
            if (collect) {
                viewModel.collectArticle(id);
                viewModel.getCollectResult().observe(this,
                        (ResponseBean responseBean) -> {
                            if (responseBean.getErrorCode() != 0) {
                                return;
                            }
//                            done();
                            ToastUtil.showToast(mContext, "收藏成功");
                            projectList.get(position).setCollect(true);
                            mAdapter.notifyItemChanged(position);
                        });
            } else {
                viewModel.unCollectArticle(id);
                viewModel.getUnCollectResult().observe(this,
                        (ResponseBean responseBean) -> {
                            if (responseBean == null) {
                                return;
                            }
//                            done();
                            ToastUtil.showToast(mContext, "取消收藏成功");
                            projectList.get(position).setCollect(false);
                            mAdapter.notifyItemChanged(position);
                        });
            }
        });
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.getProjectList(pageIndex, cid);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getProjectList(pageIndex = 0, cid);
            }
        });
        startLoading(R.id.content);
        initData();
    }

    private void initData() {
        pageIndex = 1;
        viewModel.getProjectList(pageIndex, cid);
        viewModel.getResult().observe(this, (ResponseBean<ProjectListBean> result) -> {
            if (result == null) {
                return;
            }
            done();
            if (pageIndex == 0) {
                mAdapter.getDatas().clear();
            }
            ProjectListBean bean = result.getData();
            mAdapter.getDatas().addAll(bean.getDatas());
            mAdapter.notifyDataSetChanged();
            if (bean.isOver()) {
                mRefreshLayout.resetNoMoreData();
            } else {
                pageIndex += 1;
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
    }


}
