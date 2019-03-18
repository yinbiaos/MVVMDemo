package com.mvvm.demo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mvvm.demo.Adapter.ViewPagerAdapter;
import com.mvvm.demo.BaseActivity;
import com.mvvm.demo.R;
import com.mvvm.demo.fragment.HomeFragment;
import com.mvvm.demo.fragment.NavFragment;
import com.mvvm.demo.fragment.ProjectFragment;
import com.mvvm.demo.fragment.PubFragment;
import com.mvvm.demo.fragment.SystemFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.bottom)
    BottomNavigationView navigator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String[] mTitleStrs = {"首页", "项目", "体系", "导航", "公众号"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(ProjectFragment.newInstance());
        fragments.add(SystemFragment.newInstance());
        fragments.add(NavFragment.newInstance());
        fragments.add(PubFragment.newInstance());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        initListener();
    }

    private void initListener() {
        navigator.setOnNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
        onPageSelected(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.action_project:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.action_system:
                viewPager.setCurrentItem(2, true);
                break;
            case R.id.action_navi:
                viewPager.setCurrentItem(3, true);
                break;
            case R.id.action_pub:
                viewPager.setCurrentItem(4, true);
                break;
            default:
        }
        return true;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageScrollStateChanged(int index) {
    }

    @Override
    public void onPageSelected(int index) {
        navigator.getMenu().getItem(index).setChecked(true);
        toolbar.setTitle(mTitleStrs[index]);
    }


}
