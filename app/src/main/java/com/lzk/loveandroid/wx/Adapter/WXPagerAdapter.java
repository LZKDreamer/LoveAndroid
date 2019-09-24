package com.lzk.loveandroid.wx.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lzk.loveandroid.wx.Fragment.WXListFragment;

import java.util.List;

public class WXPagerAdapter extends FragmentStatePagerAdapter {

    private List<WXListFragment> mFragmentList;
    private List<String> mTitleList;


    public WXPagerAdapter(FragmentManager fm,List<WXListFragment> mFragmentList,List<String> mTitleList) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mTitleList = mTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0:mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }
}
