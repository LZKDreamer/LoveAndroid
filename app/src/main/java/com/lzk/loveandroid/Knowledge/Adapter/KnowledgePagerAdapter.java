package com.lzk.loveandroid.Knowledge.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lzk.loveandroid.Knowledge.Fragment.KnowledgeListFragment;

import java.util.List;

/**
 * @author LiaoZhongKai
 * @date 2019/9/18.
 */
public class KnowledgePagerAdapter extends FragmentStatePagerAdapter {
    private List<KnowledgeListFragment> mFragmentList;
    private List<String> mTitleList;

    public KnowledgePagerAdapter(FragmentManager fm, List<KnowledgeListFragment> mFragmentList, List<String> mTitleList) {
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
        return mFragmentList.size();
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
