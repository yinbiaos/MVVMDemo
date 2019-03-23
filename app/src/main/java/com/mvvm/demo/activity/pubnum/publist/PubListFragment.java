package com.mvvm.demo.activity.pubnum.publist;

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
import com.mvvm.demo.adapter.PubAddrAdapter;
import com.mvvm.demo.entity.PubAddrListBean;
import com.mvvm.demo.entity.PubAddrListChild;
import com.mvvm.demo.entity.ResponseBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzy on 2019/2/18
 * 公众号文章列表
 *
 * @author hzy
 */
public class PubListFragment extends BaseLoadAnimFragment {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    List<PubAddrListChild> mList = new ArrayList<>();
    PubAddrAdapter mAdapter;

    private static final String TAG = "PubListFragment";
    private int id;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    int page = 1;
    int pageCount = 0;

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
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
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
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (pageCount > page) {
                    page++;
                }
                viewModel.getList(id, page);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                viewModel.getList(id, page);
                refreshLayout.finishRefresh();
            }
        });
        initData();
    }

    private void initData() {
        page = 1;
        viewModel = ViewModelProviders.of(this).get(PubListViewModel.class);
        viewModel.getList(id, page);
        viewModel.getResult().observe(this, (ResponseBean<PubAddrListBean> result) -> {
            PubAddrListBean bean = result.getData();
            page = bean.getCurPage();
            pageCount = bean.getPageCount();
            if (page == 1) {
                mList.clear();
            }
            mList.addAll(bean.getDatas());
            mAdapter.notifyDataSetChanged();
        });

        viewModel.getCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                ToastUtil.showToast(mContext, responseBean.getErrorMsg());
                return;
            }
            ToastUtil.showToast(mContext, "收藏成功");
            mAdapter.getDatas().get(viewModel.getPosition()).setCollect(true);
            mAdapter.notifyItemChanged(viewModel.getPosition());
        });
        viewModel.getUnCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                ToastUtil.showToast(mContext, responseBean.getErrorMsg());
                return;
            }
            ToastUtil.showToast(mContext, "取消收藏成功");
            mAdapter.getDatas().get(viewModel.getPosition()).setCollect(false);
            mAdapter.notifyItemChanged(viewModel.getPosition());
        });
    }
}
