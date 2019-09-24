package com.lzk.loveandroid.wx.Fragment;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.wx.Adapter.WXPagerAdapter;
import com.lzk.loveandroid.wx.Bean.WXTab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WXFragment extends BaseFragment {


    @BindView(R.id.wx_tab_layout)
    TabLayout wxTabLayout;
    @BindView(R.id.wx_view_pager)
    ViewPager wxViewPager;

    private List<String> mTitleList = new ArrayList<>();
    private List<WXListFragment> mFragmentList = new ArrayList<>();
    private WXPagerAdapter mWXPagerAdapter;
    private WXTab mWXTab;

    public WXFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wx;
    }

    @Override
    public void initEventAndData() {
        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestWXTabList();
        }else {
            showErrorLayout(getString(R.string.network_error));
        }
    }

    /**
     * 获取公众号Tab
     */
    private void requestWXTabList(){
        RequestCenter.requestWXTabList(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                mWXTab = (WXTab) object;
                initTabAndViewPager();
                showPageContent();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showErrorLayout(errMsg);
            }

            @Override
            public void onError() {
                showErrorLayout(getString(R.string.error));
            }
        });
    }

    private void initTabAndViewPager(){
        for (WXTab.DataBean bean : mWXTab.getData()){
            mTitleList.add(bean.getName());
            mFragmentList.add(WXListFragment.getInstance(bean.getId()));
        }
        if (mTitleList.size() > 0){
            mWXPagerAdapter = new WXPagerAdapter(getFragmentManager(),mFragmentList,mTitleList);
            wxViewPager.setAdapter(mWXPagerAdapter);
            wxTabLayout.setupWithViewPager(wxViewPager);
        }else {
            showEmptyLayout(getString(R.string.empty));
        }
    }

    public void backToTop(){
        if (mFragmentList != null && mFragmentList.size()>0){
            mFragmentList.get(wxTabLayout.getSelectedTabPosition()).backToTop();
        }
    }
}
