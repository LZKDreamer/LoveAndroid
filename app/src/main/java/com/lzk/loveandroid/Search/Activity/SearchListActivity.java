package com.lzk.loveandroid.Search.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Search.Adapter.SearchListAdapter;
import com.lzk.loveandroid.Search.Bean.SearchResult;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.main.ArticleDetailActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchListActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.search_list_rv)
    RecyclerView searchListRv;
    @BindView(R.id.search_list_refresh_layout)
    SmartRefreshLayout searchListRefreshLayout;
    @BindView(R.id.search_list_float_btn)
    FloatingActionButton searchListFloatBtn;

    private String keyword;
    private int curPage;//当前页数
    private boolean isRefresh = true;
    private SearchListAdapter mAdapter;
    private Context mContext;
    private SearchResult mSearchResult;

    private static final String INTENT_KEYWORD = "intent_keyword";

    public static Intent newIntent(Context context,String keyword){
        Intent intent = new Intent(context,SearchListActivity.class);
        intent.putExtra(INTENT_KEYWORD,keyword);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this,R.color.colorPrimaryToolbar));
        //ActionBar
        setSupportActionBar(commonToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back_white);
        }

        if (getIntent() != null){
            keyword = getIntent().getStringExtra(INTENT_KEYWORD);
        }
        if (!TextUtils.isEmpty(keyword)){
            commonToolbarTitleTv.setText(keyword);
            if (NetworkUtil.isNetworkConnected()){
                showLoadingLayout(null);
                requestSearchArticle(curPage,keyword);
            }else {
                showErrorLayout(getString(R.string.network_error));
            }
        }

        initRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_list;
    }

    @Override
    public void reload() {
        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestSearchArticle(curPage,keyword);
        }
    }

    @OnClick(R.id.search_list_float_btn)
    public void onViewClicked() {
        searchListRv.smoothScrollToPosition(0);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new SearchListAdapter(R.layout.layout_article_item,null);
        mAdapter.openLoadAnimation();
        searchListRv.setLayoutManager(layoutManager);
        searchListRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ArticleDetailActivity.newIntent(mContext,mSearchResult.getData().getDatas().get(position).getTitle(),mSearchResult.getData().getDatas().get(position).getLink());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.article_item_collect_iv:
                        SearchResult.DataBean.DatasBean datasBean = mSearchResult.getData().getDatas().get(position);
                        if (datasBean.isCollect()){
                            requestUnCollectArticle(datasBean.getId()+"",position);
                        }else {
                            requestCollectArticle(datasBean.getId()+"",position);
                        }
                        break;
                }
            }
        });

        searchListRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                curPage++;
                requestSearchArticle(curPage,keyword);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                curPage = 0;
                requestSearchArticle(curPage,keyword);
            }
        });
    }

    /**
     * 更新RecyclerView
     * @param searchResult
     */
    private void updateRecyclerView(SearchResult searchResult){
        if (isRefresh){
            mAdapter.replaceData(searchResult.getData().getDatas());
            searchListRefreshLayout.finishRefresh();
        }else {
            mAdapter.addData(searchResult.getData().getDatas());
            searchListRefreshLayout.finishLoadMore();
        }
        showPageContent();
    }

    /**
     * 搜索文章
     * @param page
     * @param keyword
     */
    private void requestSearchArticle(int page,String keyword){
        RequestCenter.requestSearchArticle(page, keyword, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                if (isRefresh){
                    mSearchResult = (SearchResult) object;
                }else {
                    mSearchResult.getData().getDatas().addAll(((SearchResult) object).getData().getDatas());
                }
                updateRecyclerView((SearchResult) object);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showErrorLayout(errMsg);
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 收藏文章
     * @param articleId
     * @param position
     */
    private void requestCollectArticle(String articleId,int position){
        RequestCenter.requestCollectArticle(articleId, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showToastInCenter(getString(R.string.collect_success));
                mSearchResult.getData().getDatas().get(position).setCollect(true);
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
     * 取消收藏文章
     * @param articleId
     * @param position
     */
    private void requestUnCollectArticle(String articleId,int position){
        RequestCenter.requestUnCollectArticle(articleId, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showToastInCenter(getString(R.string.uncollect_success));
                mSearchResult.getData().getDatas().get(position).setCollect(false);
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
