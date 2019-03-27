package com.mvvm.demo.activity.system.subsystem;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.lib.ToastUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.mvvm.demo.BaseLoadAnimFragment;
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
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzy on 2019/2/22
 *
 * @author hzy
 */
    public class SubSystemFragment extends BaseLoadAnimFragment {

    private static final String TAG = "SubSystemFragment";

    private static SubSystemFragment instance = null;


    @BindView(R.id.rv_grid)
    RecyclerView mRvGrid;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    SubSysAdapter mGridAdapter;
    SubSysListAdapter mListAdapter;
    List<SystemDataChildBean> mGridlist = new ArrayList<>();
    List<SystemDataChildBean> mGridlist2 = new ArrayList<>();
    List<KnowledgeSystemChildBean> mListlist = new ArrayList<>();

    int page = 0;
    int cid = 0;
    int pageCount = 0;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_system, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(SubSysViewModel.class);
        initEventAndData();
    }

    protected void initEventAndData() {
        mGridlist = (List<SystemDataChildBean>) getArguments().getSerializable(TAG);
        for (SystemDataChildBean bean : mGridlist) {
            bean.setChecked(false);
        }
        mGridlist.get(0).setChecked(true);
        if (mGridlist.size() > 5) {
            SystemDataChildBean sys = new SystemDataChildBean("查看更多", false);
            mGridlist.add(5, sys);
            mGridlist2.clear();
            mGridlist2.addAll(mGridlist.subList(0, 6));
            mGridAdapter = new SubSysAdapter(getActivity(), mGridlist2);
        } else {
            mGridlist2.clear();
            mGridlist2.addAll(mGridlist);
            mGridAdapter = new SubSysAdapter(getActivity(), mGridlist2);
        }
        mRvGrid.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mRvGrid.setAdapter(mGridAdapter);
        mGridAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == 5 && mGridlist2.get(position).getName().equals("查看更多")) {
                    mGridlist2.clear();
                    mGridlist.remove(5);
                    SystemDataChildBean sys = new SystemDataChildBean("收回列表", false);
                    mGridlist.add(sys);
                    mGridlist2.addAll(mGridlist);
                    mGridAdapter.notifyDataSetChanged();
                } else if (position == mGridlist2.size() - 1 && mGridlist2.get(position).getName().equals(
                        "收回列表")) {
                    mGridlist2.clear();
                    SystemDataChildBean sys = new SystemDataChildBean("查看更多", false);
                    mGridlist.remove(mGridlist.size() - 1);
                    mGridlist.add(5, sys);
                    mGridlist2.addAll(mGridlist.subList(0, 6));
                    mGridAdapter.notifyDataSetChanged();
                } else {
                    for (SystemDataChildBean bean : mGridlist2) {
                        bean.setChecked(false);
                    }
                    mGridlist2.get(position).setChecked(true);
                    mGridAdapter.notifyDataSetChanged();

                    page = 0;
                    cid = mGridlist.get(position).getId();

                    viewModel.getData(cid, page);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
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
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
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

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (pageCount > page) {
                    page++;
                    viewModel.getData(cid, page);
                } else {
                    ToastUtils.showShort("已经没有数据了");
                }
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                viewModel.getData(cid, page);
                refreshLayout.finishRefresh();
            }
        });
        cid = mGridlist.get(0).getId();
        initData();
    }

    private void initData() {
        viewModel.getData(cid, page);
        viewModel.getResult().observe(this, (ResponseBean<KnowledgeSystem> result) -> {
            KnowledgeSystem mKnowledgeSystem=result.getData();
            pageCount = mKnowledgeSystem.getPageCount();
            if (page == 0) {mListlist.clear();}
            mListlist.addAll(mKnowledgeSystem.getDatas());
            mListAdapter.notifyDataSetChanged();
        });

        viewModel.getCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                ToastUtil.showToast(mContext, responseBean.getErrorMsg());
                return;
            }
            ToastUtil.showToast(mContext, "收藏成功");
            mListAdapter.getDatas().get(viewModel.getPosition()).setCollect(true);
            mListAdapter.notifyItemChanged(viewModel.getPosition());
        });
        viewModel.getUnCollectResult().observe(this, (ResponseBean responseBean) -> {
            if (responseBean == null || responseBean.getErrorCode() != 0) {
                ToastUtil.showToast(mContext, responseBean.getErrorMsg());
                return;
            }
            ToastUtil.showToast(mContext, "取消收藏成功");
            mListAdapter.getDatas().get(viewModel.getPosition()).setCollect(false);
            mListAdapter.notifyItemChanged(viewModel.getPosition());
        });
    }
}
