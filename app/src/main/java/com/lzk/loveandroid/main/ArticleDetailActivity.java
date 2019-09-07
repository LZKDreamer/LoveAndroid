package com.lzk.loveandroid.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Utils.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailActivity extends BaseActivity {

    private static final String INTENT_URL = "intent_url";
    private static final String INTENT_TITLE = "intent_title";
    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.page_content_view)
    FrameLayout pageContentView;
    private String url;
    private String title;
    private AgentWeb mAgentWeb;

    public static Intent newIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(INTENT_TITLE, title);
        intent.putExtra(INTENT_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.colorPrimaryToolbar));
        setSupportActionBar(commonToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back_white);
        }

        if (getIntent() != null) {
            url = getIntent().getStringExtra(INTENT_URL);
            title = getIntent().getStringExtra(INTENT_TITLE);
            commonToolbarTitleTv.setText(title);
        }

        if (NetworkUtil.isNetworkConnected()) {
            loadWeb(url);
        } else {
            showErrorLayout(getString(R.string.network_error));
        }
    }

    private void loadWeb(String url) {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((FrameLayout) pageContentView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://菜单
                if (!mAgentWeb.back()){
                    finish();
                }
                break;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    @Override
    public void reload() {
        if (NetworkUtil.isNetworkConnected()){
            showPageContent();
            loadWeb(url);
        }
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null){
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null){
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null){
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }
}
