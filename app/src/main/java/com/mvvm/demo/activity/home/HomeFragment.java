package com.mvvm.demo.activity.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvvm.demo.BaseLoadAnimFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.adapter.ArticleAdapter;
import com.mvvm.demo.entity.ArticleBean;
import com.mvvm.demo.entity.ResponseBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 首页TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class HomeFragment extends BaseLoadAnimFragment {

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        startLoading(R.id.content);
        initData();
    }

    private void initData() {
        homeViewModel = new HomeViewModel(mContext.getApplication());
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
    }
}
