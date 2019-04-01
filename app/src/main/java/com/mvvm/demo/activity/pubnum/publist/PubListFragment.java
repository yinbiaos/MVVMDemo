package com.mvvm.demo.activity.pubnum.publist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.lib.ToastUtil;
import com.mvvm.demo.BaseLazyFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.X5WebView;
import com.mvvm.demo.adapter.PubAddrAdapter;
import com.mvvm.demo.entity.PubAddrListBean;
import com.mvvm.demo.entity.PubAddrListChild;
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
 * Created by hzy on 2019/2/18
 * 公众号文章列表
 *
 * @author hzy
 */
public class PubListFragment extends BaseLazyFragment {

    private static final String TAG = "PubListFragment";

    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    int pageIndex = 1;
    private int id;

    List<PubAddrListChild> mList = new ArrayList<>();
    PubAddrAdapter mAdapter;

    private PubListViewModel viewModel;

    public static PubListFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, id);
        PubListFragment fragment = new PubListFragment();
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
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PubAddrAdapter(getActivity(), mList);
        mRvList.setAdapter(mAdapter);
        //禁用滑动事件
        mRvList.setNestedScrollingEnabled(false);
        id = getArguments().getInt(TAG);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), X5WebView.class);
                intent.putExtra("mUrl", mList.get(position).getLink());
                intent.putExtra("mTitle", mList.get(position).getTitle());
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
                viewModel.getList(id, pageIndex);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getList(id, pageIndex = 1);
            }
        });
    }

    private void initData() {
        viewModel = ViewModelProviders.of(this).get(PubListViewModel.class);
        viewModel.getList(id, pageIndex);
        viewModel.getResult().observe(this, (ResponseBean<PubAddrListBean> result) -> {
            if (result == null) {
                return;
            }
            if (pageIndex == 1) {
                mList.clear();
            }
            PubAddrListBean bean = result.getData();
            mList.addAll(bean.getDatas());
            mAdapter.notifyDataSetChanged();
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
            mAdapter.getDatas().get(viewModel.getPosition()).setCollect(true);
            mAdapter.notifyItemChanged(viewModel.getPosition());
        });
        viewModel.getUnCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                return;
            }
            ToastUtil.showToast(mContext, "取消收藏成功");
            mAdapter.getDatas().get(viewModel.getPosition()).setCollect(false);
            mAdapter.notifyItemChanged(viewModel.getPosition());
        });
    }

    @Override
    protected void onLazyLoad() {
        initData();
    }
}
