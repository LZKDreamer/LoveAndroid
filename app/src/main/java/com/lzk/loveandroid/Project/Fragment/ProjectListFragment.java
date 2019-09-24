package com.lzk.loveandroid.Project.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Project.Adapter.ProjectListAdapter;
import com.lzk.loveandroid.Project.Bean.ProjectList;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.main.ArticleDetailActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectListFragment extends BaseFragment {

    private static final String BUNDLE_ID = "bundle_id";
    @BindView(R.id.project_list_rv)
    RecyclerView projectListRv;
    @BindView(R.id.project_list_refresh_layout)
    SmartRefreshLayout projectListRefreshLayout;

    private int id;
    private int page = 1;
    private boolean isRefresh =true;
    private ProjectListAdapter mAdapter;

    public static ProjectListFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID, id);
        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project_list;
    }

    @Override
    protected void initEventAndData() {
        if (getArguments() != null){
            id = getArguments().getInt(BUNDLE_ID);
        }

        initRecyclerView();
        initRefreshLayout();

        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestProjectList(id,page);
        }else {
            showErrorLayout(getString(R.string.network_error));
        }
    }

    private void initRecyclerView(){
        List<ProjectList.DataBean.DatasBean> list = new ArrayList<>();
        mAdapter = new ProjectListAdapter(R.layout.layout_project_list_adapter,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        projectListRv.setLayoutManager(layoutManager);
        projectListRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ArticleDetailActivity.newIntent(getActivity(),
                        mAdapter.getData().get(position).getTitle()
                ,mAdapter.getData().get(position).getLink());
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

    private void initRefreshLayout(){
        projectListRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                page++;
                requestProjectList(id,page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                requestProjectList(id,page);

            }
        });
    }

    private void requestProjectList(int id,int page){
        RequestCenter.requestProjectList(id, page, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                ProjectList projectList = (ProjectList) object;
                if (isRefresh){
                    mAdapter.replaceData(projectList.getData().getDatas());
                    projectListRefreshLayout.finishRefresh();
                    showPageContent();
                }else {
                    mAdapter.addData(projectList.getData().getDatas());
                    projectListRefreshLayout.finishLoadMore();
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
        RequestCenter.requestCollectArticle(articleId, new IResultCallback() {
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

    public void backToTop(){
        if (projectListRv != null){
            projectListRv.smoothScrollToPosition(0);
        }
    }
}
