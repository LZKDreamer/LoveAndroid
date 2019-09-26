package com.lzk.loveandroid.Navigation.Fragment;


import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Navigation.Adapter.NavAdapter;
import com.lzk.loveandroid.Navigation.Bean.NavigationBean;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavFragment extends BaseFragment {


    @BindView(R.id.nav_tab_layout)
    VerticalTabLayout navTabLayout;
    @BindView(R.id.nav_rv)
    RecyclerView navRv;
    @BindView(R.id.page_content_view)
    FrameLayout pageContentView;
    private NavAdapter mNavAdapter;
    private LinearLayoutManager layoutManager;

    public NavFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    public void initEventAndData() {
        initRecyclerView();
        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestNavigationData();
        }
    }

    private void initRecyclerView(){
        List<NavigationBean.DataBean> list = new ArrayList<>();
        mNavAdapter = new NavAdapter(R.layout.layout_nav_adapter_item,list);
        layoutManager = new LinearLayoutManager(getActivity());
        navRv.setLayoutManager(layoutManager);
        navRv.setAdapter(mNavAdapter);
    }

    private void initVerticalTabLayout(List<NavigationBean.DataBean> list){
        navTabLayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                return new TabView.TabTitle.Builder()
                        .setContent(list.get(position).getName())
                        .setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary),
                                ContextCompat.getColor(getActivity(),R.color.tab_item_default)).build();
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
        });
    }

    private void requestNavigationData(){
        RequestCenter.requestNavgationData(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                NavigationBean bean = (NavigationBean) object;
                mNavAdapter.replaceData(bean.getData());
                initVerticalTabLayout(bean.getData());
                showPageContent();
                leftRightLinkage();
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

    /**
     * 左右联动
     */
    private void leftRightLinkage(){
        navTabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                layoutManager.scrollToPositionWithOffset(position,0);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });

        navRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                navTabLayout.setTabSelected(layoutManager.findFirstVisibleItemPosition());
            }
        });
    }

    public void backToTop(){
        if (navRv != null && navTabLayout!=null){
            navRv.smoothScrollToPosition(0);
        }
    }
}
