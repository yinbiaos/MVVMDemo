package com.mvvm.demo.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.mvvm.demo.BaseActivity;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.home.HomeFragment;
import com.mvvm.demo.activity.navigat.NaviFragment;
import com.mvvm.demo.activity.project.ProjectFragment;
import com.mvvm.demo.activity.pubnum.PublicAddrFragment;
import com.mvvm.demo.activity.system.SystemFragment;
import com.mvvm.demo.adapter.ViewPagerAdapter;

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
        fragments.add(NaviFragment.newInstance());
        fragments.add(PublicAddrFragment.getInstance());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(fragments.size());
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
