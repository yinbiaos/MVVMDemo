package com.mvvm.demo.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> listTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> listTitle) {
        super(fm);
        this.mFragments = mFragments;
        this.listTitle = listTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}