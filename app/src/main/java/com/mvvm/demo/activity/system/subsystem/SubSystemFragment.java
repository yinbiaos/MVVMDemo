package com.mvvm.demo.activity.system.subsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.lib.ToastUtil;
import com.mvvm.demo.BaseLazyFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.X5WebView;
import com.mvvm.demo.adapter.SubSysAdapter;
import com.mvvm.demo.adapter.SubSysListAdapter;
import com.mvvm.demo.entity.KnowledgeSystem;
import com.mvvm.demo.entity.KnowledgeSystemChildBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.entity.SystemDataChildBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by hzy on 2019/2/22
 *
 * @author hzy
 */
public class SubSystemFragment extends BaseLazyFragment {

    private static final String TAG = "SubSystemFragment";

    @BindView(R.id.rv_grid)
    RecyclerView mRvGrid;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    SubSysAdapter mGridAdapter;
    SubSysListAdapter mListAdapter;
    List<SystemDataChildBean> mGridlist = new ArrayList<>();
    List<KnowledgeSystemChildBean> mListlist = new ArrayList<>();

    int pageIndex = 0;
    int cid = 0;

    private SubSysViewModel viewModel;

    public static SubSystemFragment newInstance(List<SystemDataChildBean> list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, (Serializable) list);
        SubSystemFragment fragment = new SubSystemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_system, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SubSysViewModel.class);
    }

    @Override
    protected void onLazyLoad() {
        initEventAndData();
    }

    protected void initEventAndData() {
        mGridlist = (List<SystemDataChildBean>) getArguments().getSerializable(TAG);
        mGridAdapter = new SubSysAdapter(mContext, mGridlist);
        mRvGrid.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRvGrid.setAdapter(mGridAdapter);

        if (mGridlist.size() > 5) {
            SystemDataChildBean sys = new SystemDataChildBean("查看更多", false);
            mGridlist.add(5, sys);
            mGridAdapter.setItemCount(6);
        } else {
            mGridAdapter.setItemCount(mGridlist.size());
        }

        mGridAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String text = mGridAdapter.getDatas().get(position).getName();
                if (position == 5 && "查看更多".equals(text)) {
                    mGridlist.remove(5);
                    SystemDataChildBean sys = new SystemDataChildBean("收回列表", false);
                    mGridlist.add(sys);
                    mGridAdapter.setItemCount(mGridlist.size());
                } else if (position == mGridAdapter.getItemCount() - 1 && "收回列表".equals(text)) {
                    SystemDataChildBean sys = new SystemDataChildBean("查看更多", false);
                    mGridlist.remove(mGridlist.size() - 1);
                    mGridlist.add(5, sys);
                    mGridAdapter.setItemCount(6);
                } else {
                    mGridAdapter.setSelectItem(position);
                    cid = mGridlist.get(position).getId();
                    mListAdapter.clear();
                    viewModel.getData(pageIndex = 0, cid);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mListAdapter = new SubSysListAdapter(getActivity(), mListlist);
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvList.setAdapter(mListAdapter);
        //禁用滑动事件
        mRvList.setNestedScrollingEnabled(false);
        mListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), X5WebView.class);
                intent.putExtra("mUrl", mListlist.get(position).getLink());
                intent.putExtra("mTitle", mListlist.get(position).getTitle());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mListAdapter.setmOnCollectListener((collect, id, position) -> {
            if (collect) {
                viewModel.unCollectArticle(id, position);
            } else {
                viewModel.collectArticle(id, position);
            }
        });

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.getData(pageIndex, cid);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getData(pageIndex = 0, cid);
            }
        });

        viewModel.getResult().observe(this, (ResponseBean<KnowledgeSystem> result) -> {
            if (result == null) {
                return;
            }
            if (pageIndex == 0) {
                mListlist.clear();
            }
            KnowledgeSystem bean = result.getData();
            mListlist.addAll(bean.getDatas());
            mListAdapter.notifyDataSetChanged();

            mRefreshLayout.finishRefresh();
            if (bean.isOver()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                pageIndex += 1;
                mRefreshLayout.finishLoadMore();
            }
        });

        viewModel.getCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            ToastUtil.showToast(mContext, "收藏成功");
            mListAdapter.getDatas().get(viewModel.getPosition()).setCollect(true);
            mListAdapter.notifyItemChanged(viewModel.getPosition());
        });
        viewModel.getUnCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            ToastUtil.showToast(mContext, "取消收藏成功");
            mListAdapter.getDatas().get(viewModel.getPosition()).setCollect(false);
            mListAdapter.notifyItemChanged(viewModel.getPosition());
        });

        mGridAdapter.setSelectItem(0);
        cid = mGridlist.get(0).getId();
        viewModel.getData(pageIndex, cid);
    }

}
