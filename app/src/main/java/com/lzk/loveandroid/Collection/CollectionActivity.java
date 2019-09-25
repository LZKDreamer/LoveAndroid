package com.lzk.loveandroid.Collection;

import android.content.Intent;
import android.os.Bundle;
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
import com.lzk.loveandroid.Collection.Adapter.FavoriteArticleAdapter;
import com.lzk.loveandroid.Collection.Bean.FavoriteArticle;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView mCommonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar mCommonToolbar;
    @BindView(R.id.collection_rv)
    RecyclerView mCollectionRv;
    @BindView(R.id.collection_refresh_layout)
    SmartRefreshLayout mCollectionRefreshLayout;
    @BindView(R.id.collection_float_btn)
    FloatingActionButton mCollectionFloatBtn;

    private int page = 0;
    private boolean isRefresh = true;
    private FavoriteArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.colorPrimaryToolbar));
        ButterKnife.bind(this);
        setSupportActionBar(mCommonToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back_white);
        }
        mCommonToolbarTitleTv.setText(getResources().getString(R.string.drawer_nav_favorite));

        initRecyclerView();
        initRefreshLayout();

        if (NetworkUtil.isNetworkConnected()) {
            showLoadingLayout(null);
            requestCollectArticle(page);
        } else {
            showErrorLayout(getString(R.string.network_error));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initRecyclerView() {
        List<FavoriteArticle.DataBean.DatasBean> list = new ArrayList<>();
        mAdapter = new FavoriteArticleAdapter(R.layout.layout_favorite_adapter_item, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCollectionRv.setLayoutManager(layoutManager);
        mCollectionRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ArticleDetailActivity.newIntent(CollectionActivity.this, mAdapter.getData().get(position).getTitle(),
                        mAdapter.getData().get(position).getLink());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                requestUnCollectFavoriteArticle(mAdapter.getData().get(position).getId(),
                        mAdapter.getData().get(position).getOriginId(), position);
            }
        });
    }

    private void initRefreshLayout() {
        mCollectionRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                page++;
                requestCollectArticle(page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 0;
                requestCollectArticle(page);
            }
        });
    }

    private void requestCollectArticle(int page) {
        RequestCenter.requestCollectionArticle(page, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                FavoriteArticle favoriteArticle = (FavoriteArticle) object;
                if (isRefresh) {
                    mAdapter.replaceData(favoriteArticle.getData().getDatas());
                    mCollectionRefreshLayout.finishRefresh();
                    showPageContent();
                } else {
                    mAdapter.addData(favoriteArticle.getData().getDatas());
                    mCollectionRefreshLayout.finishLoadMore();
                }
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

    private void requestUnCollectFavoriteArticle(int id, int originId, int position) {
        RequestCenter.requestUnCollectFavoriteArticle(id, originId, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                mAdapter.remove(position);
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

    @OnClick(R.id.collection_float_btn)
    public void onViewClicked() {
        if (mCollectionRv != null){
            mCollectionRv.smoothScrollToPosition(0);
        }
    }
}
