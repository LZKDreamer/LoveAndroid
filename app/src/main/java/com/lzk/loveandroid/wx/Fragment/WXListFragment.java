package com.lzk.loveandroid.wx.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.main.ArticleDetailActivity;
import com.lzk.loveandroid.wx.Adapter.WXListAdapter;
import com.lzk.loveandroid.wx.Bean.WXArticle;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WXListFragment extends BaseFragment {

    private static final String BUNDLE_ID = "bundle_id";
    @BindView(R.id.wx_list_rv)
    RecyclerView wxListRv;
    @BindView(R.id.wx_list_refresh_layout)
    SmartRefreshLayout wxListRefreshLayout;

    private int id;
    private int page = 1;
    private boolean isRefresh = true;
    private WXListAdapter mAdapter;

    public static WXListFragment getInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID, id);
        WXListFragment fragment = new WXListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wxlist;
    }

    @Override
    public void initEventAndData() {
        initRecyclerView();
        initRefreshLayout();

        if (getArguments() != null) {
            id = getArguments().getInt(BUNDLE_ID);
        }

        if (NetworkUtil.isNetworkConnected()) {
            showLoadingLayout(null);
            requestWXArticle(id, page);
        } else {
            showErrorLayout(getString(R.string.network_error));
        }
    }

    private void initRecyclerView() {
        List<WXArticle.DataBean.DatasBean> list = new ArrayList<>();
        mAdapter = new WXListAdapter(R.layout.layout_content_item, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        wxListRv.setLayoutManager(layoutManager);
        wxListRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ArticleDetailActivity.newIntent(getActivity(),
                        mAdapter.getData().get(position).getTitle(),
                        mAdapter.getData().get(position).getLink());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getData().get(position).isCollect()){
                    requestUnCollectArticle(mAdapter.getData().get(position).getId()+"",position);
                }else {
                    requestCollectArticle(mAdapter.getData().get(position).getId()+"",position);
                }
            }
        });
    }

    private void initRefreshLayout() {
        wxListRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                page++;
                requestWXArticle(id,page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                requestWXArticle(id,page);
            }
        });
    }

    /**
     * 获取
     *
     * @param id   公众号id
     * @param page
     */
    private void requestWXArticle(int id, int page) {
        RequestCenter.requestWXArticle(id, page, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                WXArticle mWXArticle = (WXArticle) object;
                if (isRefresh) {
                    mAdapter.replaceData(mWXArticle.getData().getDatas());
                    wxListRefreshLayout.finishRefresh();
                    showPageContent();
                }else {
                    mAdapter.addData(mWXArticle.getData().getDatas());
                    wxListRefreshLayout.finishLoadMore();
                }
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

    public void backToTop(){
        if (wxListRv != null){
            wxListRv.smoothScrollToPosition(0);
        }
    }

    private void requestCollectArticle(String articleId,int position){
        RequestCenter.requestCollectArticle(articleId, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                mAdapter.getData().get(position).setCollect(true);
                mAdapter.setData(position,mAdapter.getData().get(position));
                showToastInCenter(getString(R.string.collect_success));
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showToastInCenter(errMsg);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void requestUnCollectArticle(String articleId,int position){
        RequestCenter.requestUnCollectArticle(articleId, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                mAdapter.getData().get(position).setCollect(false);
                mAdapter.setData(position,mAdapter.getData().get(position));
                showToastInCenter(getString(R.string.uncollect_success));
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showToastInCenter(errMsg);
            }

            @Override
            public void onError() {

            }
        });
    }
}
