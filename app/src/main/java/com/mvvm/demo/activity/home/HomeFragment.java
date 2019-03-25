package com.mvvm.demo.activity.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.lib.Logs;
import com.mvvm.demo.BaseLoadAnimFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.X5WebView;
import com.mvvm.demo.adapter.ArticleAdapter;
import com.mvvm.demo.entity.ArticleBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.event.LoginSuccessEvent;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 首页TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class HomeFragment extends BaseLoadAnimFragment implements MultiItemTypeAdapter.OnItemClickListener, OnRefreshLoadMoreListener {

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
        EventBus.getDefault().register(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter = new ArticleAdapter(mContext, new ArrayList<>()));
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setmOnCollectListener((collect, id, position) -> {
            progressDialog.show();
            if (collect) {
                homeViewModel.unCollectArticle(id, position);
            } else {
                homeViewModel.collectArticle(id, position);
            }
        });
        startLoading(R.id.content);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
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
                mRefreshLayout.resetNoMoreData();
            } else {
                pageIndex += 1;
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });

        homeViewModel.getCollectResult().observe(this, (ResponseBean responseBean) -> {
            progressDialog.cancel();
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            showTipsDialog(QMUITipDialog.Builder.ICON_TYPE_SUCCESS, "收藏成功");
            adapter.getDatas().get(homeViewModel.getPosition()).setCollect(true);
            adapter.notifyItemChanged(homeViewModel.getPosition());
        });
        homeViewModel.getUnCollectResult().observe(this, (ResponseBean responseBean) -> {
            progressDialog.cancel();
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            showTipsDialog(QMUITipDialog.Builder.ICON_TYPE_SUCCESS, "取消收藏成功");
            adapter.getDatas().get(homeViewModel.getPosition()).setCollect(false);
            adapter.notifyItemChanged(homeViewModel.getPosition());
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(LoginSuccessEvent event) {
        Logs.d(TAG, "loginSuccess:刷新首页数据");
        homeViewModel.getArticle(pageIndex = 0);
    }

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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        homeViewModel.getArticle(pageIndex);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        homeViewModel.getArticle(pageIndex = 0);
    }
}
