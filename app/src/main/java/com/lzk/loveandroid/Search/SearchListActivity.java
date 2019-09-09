package com.lzk.loveandroid.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
                requestSearchArticle(0,keyword);
            }else {
                showErrorLayout(getString(R.string.network_error));
            }
        }
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
        }
    }

    @OnClick(R.id.search_list_float_btn)
    public void onViewClicked() {
        searchListRv.smoothScrollToPosition(0);
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

            }

            @Override
            public void onFailure(int errCode, String errMsg) {

            }

            @Override
            public void onError() {

            }
        });
    }
}
