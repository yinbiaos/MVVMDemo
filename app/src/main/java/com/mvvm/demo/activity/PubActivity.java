package com.mvvm.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.mvvm.demo.BaseActivity;
import com.mvvm.demo.R;
import com.mvvm.demo.adapter.PubAdapter;
import com.mvvm.demo.widget.TitleBarLayout;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hzy on 2019/2/20
 * <p>
 * tab 选择页面
 *
 * @author Administrator
 */
public class PubActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBarLayout mTitleBar;

    @BindView(R.id.rv_pub)
    RecyclerView mRvPub;

    PubAdapter mAdapter;
    List<String> titleList = new ArrayList<>();
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pub;
    }


    protected void initView() {
        title = getIntent().getStringExtra("title");
        titleList = getIntent().getStringArrayListExtra("titleList");
        mTitleBar.setTitleBarBackgroundColor(getResources().getColor(R.color.c_6c8cff));
        mTitleBar.setTitleColor(getResources().getColor(R.color.c_ffffff));
        mTitleBar.setTitle(title);
        mTitleBar.setLeftBack(v -> finish());
        initData();
    }

    protected void initData() {
        mRvPub.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PubAdapter(this, titleList);
        mRvPub.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
                return false;
            }
        });
    }
}
