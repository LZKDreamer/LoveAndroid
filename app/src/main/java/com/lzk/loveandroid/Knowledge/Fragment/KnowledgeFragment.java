package com.lzk.loveandroid.Knowledge.Fragment;


import android.content.Intent;
import android.renderscript.Allocation;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Knowledge.Activity.KnowledgeListActivity;
import com.lzk.loveandroid.Knowledge.Adapter.KnowledgeAdapter;
import com.lzk.loveandroid.Knowledge.Bean.KnowledgeBean;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.LogUtil;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeFragment extends BaseFragment {

    @BindView(R.id.knowledge_rv)
    RecyclerView mKnowledgeRv;
    @BindView(R.id.knowledge_refresh_layout)
    SmartRefreshLayout mKnowledgeRefreshLayout;

    private KnowledgeBean mKnowledgeBean;

    private KnowledgeAdapter mAdapter;

    public KnowledgeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_knowledge;
    }

    @Override
    public void initEventAndData() {
        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestKnowledgeSystem();
        }else {
            showErrorLayout(getString(R.string.network_error));
        }

        mKnowledgeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (NetworkUtil.isNetworkConnected()){
                    requestKnowledgeSystem();
                }else {
                    mKnowledgeRefreshLayout.finishRefresh();
                }
            }
        });
    }

    /**
     * 设置RecyclerView
     */
    private void setRecyclerView(){
        if (mAdapter == null){
            mAdapter = new KnowledgeAdapter(R.layout.layout_knowledge_item,mKnowledgeBean.getData());
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            mKnowledgeRv.setLayoutManager(layoutManager);
            mKnowledgeRv.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = KnowledgeListActivity.newIntent(getActivity(),mKnowledgeBean.getData().get(position));
                    startActivity(intent);
                }
            });
        }else {
            mAdapter.replaceData(mKnowledgeBean.getData());
        }
    }

    /**
     * 知识体系
     */
    private void requestKnowledgeSystem(){
        RequestCenter.requestKnowledgeSystem(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showPageContent();
                mKnowledgeBean = (KnowledgeBean) object;
                setRecyclerView();
                mKnowledgeRefreshLayout.finishRefresh();
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
     * 回到顶部
     */
    public void backToTop(){
        if (mKnowledgeRv != null){
            mKnowledgeRv.smoothScrollToPosition(0);
        }
    }

}
