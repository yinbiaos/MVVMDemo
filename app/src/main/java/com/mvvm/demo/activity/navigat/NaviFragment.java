package com.mvvm.demo.activity.navigat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvvm.demo.BaseLoadAnimFragment;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.X5WebView;
import com.mvvm.demo.adapter.NaviAdapter;
import com.mvvm.demo.adapter.NaviGridAdapter;
import com.mvvm.demo.entity.NaviBean;
import com.mvvm.demo.entity.NaviChildBean;
import com.mvvm.demo.entity.ResponseBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzy on 2019/1/22
 * 导航tab
 *
 * @author hzy
 */
public class NaviFragment extends BaseLoadAnimFragment {

    public static final String TAG = "NaviFragment";
    private static NaviFragment instance = null;


    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.rv_grid)
    RecyclerView mRvGrid;

    List<NaviBean> naviList = new ArrayList<>();
    NaviAdapter mAdapter;

    List<NaviChildBean> naviGridList = new ArrayList<>();
    NaviGridAdapter mGridAdapter;

    private NaviViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
    }

    public static NaviFragment getInstance() {
        if (instance == null) {
            instance = new NaviFragment();
        }
        return instance;
    }

    public static NaviFragment getInstance(String title) {
        if (instance == null) {
            Bundle bundle = new Bundle();
            bundle.putString(TAG, title);
            instance = new NaviFragment();
            instance.setArguments(bundle);
        }
        return instance;
    }

    protected void initEventAndData() {
        viewModel= ViewModelProviders.of(this).get(NaviViewModel.class);
        viewModel.getNavi();
        viewModel.getResult().observe(this, (ResponseBean<List<NaviBean>> result) -> {
            List<NaviBean> naviBeanList = result.getData();
            if (result == null) {
                return;
            }
            updateView(naviBeanList);
        });
    }

    public void updateView(List<NaviBean> naviBeanList) {
        Log.e("NaviFragment", naviBeanList.toString());
        Log.d("updateProject", naviBeanList.toString());
        naviList.addAll(naviBeanList);
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        naviList.get(0).setChecked(true);
        mAdapter = new NaviAdapter(getActivity(), naviList);
        mRvList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                for (NaviBean bean : naviList) {
                    bean.setChecked(false);
                }
                naviList.get(position).setChecked(true);
                mAdapter.notifyDataSetChanged();

                naviGridList.clear();
                naviGridList.addAll(naviBeanList.get(position).getArticles());
                mGridAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
                return false;
            }
        });

        if (null != naviBeanList && naviBeanList.size() > 0) {
            naviGridList.addAll(naviBeanList.get(0).getArticles());
            mRvGrid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mGridAdapter = new NaviGridAdapter(getActivity(), naviGridList);
            mRvGrid.setAdapter(mGridAdapter);
            mGridAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(getActivity(), X5WebView.class);
                    intent.putExtra("mUrl", naviGridList.get(position).getLink());
                    intent.putExtra("mTitle", naviGridList.get(position).getTitle());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                               int position) {
                    return false;
                }
            });
        }


    }
}
