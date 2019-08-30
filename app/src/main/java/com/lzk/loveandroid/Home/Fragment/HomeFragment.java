package com.lzk.loveandroid.Home.Fragment;


import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    @BindView(R.id.home_rv)
    RecyclerView mHomeRv;
    @BindView(R.id.home_refresh_layout)
    SmartRefreshLayout mHomeRefreshLayout;
    @BindView(R.id.page_content_view)
    FrameLayout mPageContentView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initEventAndData() {
        showLoadingLayout(null);
    }

    @Override
    public void reload() {

    }

}
