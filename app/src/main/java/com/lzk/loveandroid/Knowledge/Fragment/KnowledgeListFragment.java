package com.lzk.loveandroid.Knowledge.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Knowledge.Adapter.KnowledgeItemAdapter;
import com.lzk.loveandroid.Knowledge.Bean.KnowledgeItem;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.LogUtil;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.main.ArticleDetailActivity;
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
        if (getArguments() != null){
            id = getArguments().getInt(INTENT_INDEX_ID);
        }

        initRecyclerView();

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
     * 初始化RecyclerView
     */
    private void initRecyclerView(){
        mAdapter = new KnowledgeItemAdapter(R.layout.layout_content_item,null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mKnowledgeListRv.setLayoutManager(layoutManager);
        mKnowledgeListRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ArticleDetailActivity.newIntent(getActivity(),mKnowledgeItem.getData().getDatas().get(position).getTitle(),
                        mKnowledgeItem.getData().getDatas().get(position).getLink());
                startActivity(intent);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.content_item_collect_iv:
                        if (mKnowledgeItem.getData().getDatas().get(position).isCollect()){
                            requestUnCollectArticle(mKnowledgeItem.getData().getDatas().get(position).getId()+"",position);
                        }else {
                            requestCollectArticle(mKnowledgeItem.getData().getDatas().get(position).getId()+"",position);
                        }
                        break;
                }
            }
        });
    }

    /**
     * 获取知识体系下的文章
     * @param id
     * @param page
     */
    private void requestKnowledgeItem(int id,int page){
        RequestCenter.requestKnowldgeItem(id, page, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                if (isRefresh){
                    mKnowledgeItem = (KnowledgeItem) object;
                    mAdapter.replaceData(mKnowledgeItem.getData().getDatas());
                    mKnowledgeListRefreshLayout.finishRefresh();
                    showPageContent();
                }else {
                    mAdapter.addData(((KnowledgeItem)object).getData().getDatas());
                    mKnowledgeItem.getData().getDatas().addAll(((KnowledgeItem)object).getData().getDatas());
                    mKnowledgeListRefreshLayout.finishLoadMore();
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

    /**
     * 返回顶部
     */
    public void backToTop(){
        if (mKnowledgeListRv != null){
            mKnowledgeListRv.smoothScrollToPosition(0);
        }
    }

    /**
     * 收藏文章
     * @param articleId
     */
    private void requestCollectArticle(String articleId,int position){
        RequestCenter.requestCollectArticle(articleId, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showToastInCenter(getString(R.string.collect_success));
                mKnowledgeItem.getData().getDatas().get(position).setCollect(true);
                mAdapter.notifyItemChanged(position);
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

    /**
     * 取消收藏
     * @param articleId
     */
    private void requestUnCollectArticle(String articleId,int position){
        RequestCenter.requestUnCollectArticle(articleId, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showToastInCenter(getString(R.string.uncollect_success));
                mKnowledgeItem.getData().getDatas().get(position).setCollect(false);
                mAdapter.notifyItemChanged(position);
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
