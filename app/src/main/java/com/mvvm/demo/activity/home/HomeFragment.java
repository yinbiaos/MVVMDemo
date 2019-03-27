package com.mvvm.demo.activity.home;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.lib.Logs;
import com.base.lib.ToastUtil;
import com.mvvm.demo.BaseLazyFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.X5WebView;
import com.mvvm.demo.adapter.ArticleAdapter;
import com.mvvm.demo.entity.ArticleBean;
import com.mvvm.demo.entity.ResponseBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 首页TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class HomeFragment extends BaseLazyFragment {

    private static final String TAG = "HomeFragment";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mRefreshLayout;

    private HomeViewModel homeViewModel;
    private ArticleAdapter adapter;

    private int pageIndex = 0;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter = new ArticleAdapter(mContext, new ArrayList<>()));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                homeViewModel.getArticle(pageIndex);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                homeViewModel.getArticle(pageIndex = 0);
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext, X5WebView.class);
                intent.putExtra("mUrl", adapter.getDatas().get(position).getLink());
                intent.putExtra("mTitle", adapter.getDatas().get(position).getTitle());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        adapter.setmOnCollectListener((collect, id, position) -> {
            if (collect) {
                homeViewModel.unCollectArticle(id, position);
            } else {
                homeViewModel.collectArticle(id, position);
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        Logs.d(TAG, "onLazyLoad");
        startLoading(R.id.content);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getArticle(pageIndex);
        homeViewModel.getResult().observe(this, (ResponseBean<ArticleBean> result) -> {
            if (result == null) {
                return;
            }
            done();
            if (pageIndex == 0) {
                adapter.getDatas().clear();
            }
            ArticleBean bean = result.getData();
            adapter.getDatas().addAll(bean.getDatas());
            adapter.notifyDataSetChanged();
            if (bean.isOver()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                pageIndex += 1;
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });

        homeViewModel.getCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            ToastUtil.showToast(mContext, "收藏成功");
            adapter.getDatas().get(homeViewModel.getPosition()).setCollect(true);
            adapter.notifyItemChanged(homeViewModel.getPosition());
        });
        homeViewModel.getUnCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            ToastUtil.showToast(mContext, "取消收藏成功");
            adapter.getDatas().get(homeViewModel.getPosition()).setCollect(false);
            adapter.notifyItemChanged(homeViewModel.getPosition());
        });
    }

}
