package com.mvvm.demo.fragment;

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
import com.mvvm.demo.BaseFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.adapter.ArticleAdapter;
import com.mvvm.demo.entity.ArticleBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.viewmodel.HomeViewModel;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 首页TAB
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HomeViewModel homeViewModel;
    ArticleAdapter adapter;

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
        initData();
    }

    private void initData() {
        homeViewModel = new HomeViewModel(mContext.getApplication());
        homeViewModel.getResult().observe(this, (ResponseBean<ArticleBean> result) -> {
            adapter.addList(result.getData().getDatas());
        });
        homeViewModel.getArticle(1);
    }
}
