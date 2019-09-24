package com.lzk.loveandroid.Project.Fragment;


import android.provider.FontRequest;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Project.Adapter.ProjectPagerAdapter;
import com.lzk.loveandroid.Project.Bean.ProjectTab;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;

import java.io.Flushable;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends BaseFragment {


    @BindView(R.id.project_tab_layout)
    TabLayout projectTabLayout;
    @BindView(R.id.project_view_pager)
    ViewPager projectViewPager;
    private ProjectPagerAdapter mPagerAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<ProjectListFragment> mFragmentList = new ArrayList<>();

    public ProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    public void initEventAndData() {
        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestProjectTabList();
        }else {
            showErrorLayout(getString(R.string.network_error));
        }
    }

    @Override
    public void reload() {
        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestProjectTabList();
        }
    }

    private void initTabAndViewPager(List<ProjectTab.DataBean> list){
        mTitleList.clear();
        mFragmentList.clear();
        for (ProjectTab.DataBean tab : list){
            mTitleList.add(tab.getName());
            mFragmentList.add(ProjectListFragment.newInstance(tab.getId()));
        }
        mPagerAdapter = new ProjectPagerAdapter(getFragmentManager(),mTitleList, mFragmentList);
        projectViewPager.setAdapter(mPagerAdapter);
        projectTabLayout.setupWithViewPager(projectViewPager);
    }

    private void requestProjectTabList(){
        RequestCenter.requestProjectTabList(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                ProjectTab tab = (ProjectTab) object;
                initTabAndViewPager(tab.getData());
                showPageContent();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {

            }

            @Override
            public void onError() {

            }
        });
    }

    public void backToTop(){
        if (mFragmentList != null && mFragmentList.size()>0){
            mFragmentList.get(projectTabLayout.getSelectedTabPosition()).backToTop();
        }
    }
}
