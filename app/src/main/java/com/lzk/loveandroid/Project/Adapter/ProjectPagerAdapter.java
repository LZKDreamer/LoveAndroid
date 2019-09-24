package com.lzk.loveandroid.Project.Adapter;

import android.service.autofill.LuhnChecksumValidator;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lzk.loveandroid.Project.Fragment.ProjectListFragment;

import java.util.List;

public class ProjectPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitleList;
    private List<ProjectListFragment> mFragmentList;

    public ProjectPagerAdapter(FragmentManager fm,List<String> mTitleList,List<ProjectListFragment> mFragmentList) {
        super(fm);
        this.mTitleList = mTitleList;
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTitleList == null?0:mTitleList.size();
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
