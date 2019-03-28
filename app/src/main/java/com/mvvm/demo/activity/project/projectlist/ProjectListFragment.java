package com.mvvm.demo.activity.project.projectlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.lib.ToastUtil;
import com.mvvm.demo.BaseLazyFragment;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by hzy on 2019/2/20
 * 项目列表
 *
 * @author hzy
 */

public class ProjectListFragment extends BaseLazyFragment {

    private static final String TAG = "ProjectListFragment";

    @BindView(R.id.rv_list)
    RecyclerView mRvProject;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    /**
     * 项目列表的页码从1开始
     */
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
    }

    protected void initEventAndData() {
        viewModel = ViewModelProviders.of(this).get(ProjectListViewModel.class);
        if (getArguments() != null) {
            cid = getArguments().getInt(TAG);
        }
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
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mAdapter.setmOnCollectListener((collect, id, position) -> {
            if (collect) {
                viewModel.unCollectArticle(id, position);
            } else {
                viewModel.collectArticle(id, position);
            }
        });
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.getProjectList(pageIndex, cid);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getProjectList(pageIndex = 1, cid);
            }
        });
    }

    private void initData() {
        viewModel.getProjectList(pageIndex, cid);
        viewModel.getResult().observe(this, (ResponseBean<ProjectListBean> result) -> {
            if (result == null) {
                return;
            }
            done();
            if (pageIndex == 1) {
                mAdapter.getDatas().clear();
            }
            ProjectListBean bean = result.getData();
            mAdapter.getDatas().addAll(bean.getDatas());
            mAdapter.notifyDataSetChanged();
            mRefreshLayout.finishRefresh();
            if (bean.isOver()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                pageIndex += 1;
                mRefreshLayout.finishLoadMore();
            }
        });

        viewModel.getUnCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            ToastUtil.showToast(mContext, "取消收藏成功");
            projectList.get(viewModel.getPosition()).setCollect(false);
            mAdapter.notifyItemChanged(viewModel.getPosition());
        });

        viewModel.getCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            ToastUtil.showToast(mContext, "收藏成功");
            projectList.get(viewModel.getPosition()).setCollect(true);
            mAdapter.notifyItemChanged(viewModel.getPosition());
        });
    }


    @Override
    protected void onLazyLoad() {
        startLoading(R.id.content);
        initData();
    }
}
