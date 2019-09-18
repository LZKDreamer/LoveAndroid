package com.lzk.loveandroid.Knowledge.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Knowledge.Adapter.KnowledgeItemAdapter;
import com.lzk.loveandroid.Knowledge.Bean.KnowledgeItem;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.LogUtil;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeListFragment extends BaseFragment {

    @BindView(R.id.knowledge_list_rv)
    RecyclerView mKnowledgeListRv;
    @BindView(R.id.knowledge_list_refresh_layout)
    SmartRefreshLayout mKnowledgeListRefreshLayout;

    private int id;
    private int page = 0;
    private boolean isRefresh = true;
    private KnowledgeItemAdapter mAdapter;
    private KnowledgeItem mKnowledgeItem;

    private static final String INTENT_INDEX_ID = "intent_index_id";

    /**
     *
     * @param id 二级目录id
     * @return
     */
    public static KnowledgeListFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_INDEX_ID,id);
        KnowledgeListFragment fragment = new KnowledgeListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_knowledge_list;
    }

    @Override
    public void initEventAndData() {
        LogUtil.d("initEventAndData");
        if (getArguments() != null){
            id = getArguments().getInt(INTENT_INDEX_ID);
        }
        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestKnowledgeItem(id,page);
        }else {
            showErrorLayout(getString(R.string.network_error));
        }

        mKnowledgeListRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                page++;
                if (page >= mKnowledgeItem.getData().getPageCount()){
                    mKnowledgeListRefreshLayout.finishLoadMoreWithNoMoreData();
                }else {
                    requestKnowledgeItem(id,page);
                }

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 0;
                requestKnowledgeItem(id,page);
            }
        });

    }

    /**
     * 设置RecyclerView
     */
    private void setRecyclerView(){
        if (isRefresh){
            if(mAdapter == null){
                mAdapter = new KnowledgeItemAdapter(R.layout.layout_content_item,mKnowledgeItem.getData().getDatas());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                mKnowledgeListRv.setLayoutManager(layoutManager);
                mKnowledgeListRv.setAdapter(mAdapter);
            }else {
                mAdapter.replaceData(mKnowledgeItem.getData().getDatas());
            }
            mKnowledgeListRefreshLayout.finishRefresh();
        }else {
            mAdapter.addData(mKnowledgeItem.getData().getDatas());
            mKnowledgeListRefreshLayout.finishLoadMore();
        }
    }

    /**
     * 获取知识体系下的文章
     * @param id
     * @param page
     */
    private void requestKnowledgeItem(int id,int page){
        LogUtil.d("id:"+id);
        RequestCenter.requestKnowldgeItem(id, page, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                if (isRefresh){
                    mKnowledgeItem = (KnowledgeItem) object;
                }else {
                    mKnowledgeItem.getData().getDatas().addAll(((KnowledgeItem)object).getData().getDatas());
                }
                if (mKnowledgeItem.getData().getDatas().size() == 0){
                    LogUtil.d("size == 0");
                }
                setRecyclerView();
                LogUtil.d("request");
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
}
