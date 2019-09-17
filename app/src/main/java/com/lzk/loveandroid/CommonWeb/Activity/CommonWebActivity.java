package com.lzk.loveandroid.CommonWeb.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.App.MyApplication;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.CommonWeb.Bean.CommonWebBean;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.CommonUtil;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.main.ArticleDetailActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonWebActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.common_web_flow_layout)
    TagFlowLayout commonWebFlowLayout;

    private CommonWebBean mCommonWebBean;

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
        commonToolbarTitleTv.setText(getString(R.string.website));

        if (NetworkUtil.isNetworkConnected()){
            loadCommonWeb();
        }else {
            showErrorLayout(getString(R.string.network_error));
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
        return R.layout.activity_common_web;
    }

    @Override
    public void reload() {
        if (NetworkUtil.isNetworkConnected()){
            loadCommonWeb();
        }
    }

    private void loadCommonWeb(){
        showLoadingLayout(null);
        RequestCenter.requestCommonWeb(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                mCommonWebBean = (CommonWebBean) object;
                if (mCommonWebBean.getData() != null && mCommonWebBean.getData().size()>0){
                    setFlowLayout();
                }
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showErrorLayout(errMsg);
            }

            @Override
            public void onError() {
                showErrorLayout(null);
            }
        });
    }

    private void setFlowLayout(){
        commonWebFlowLayout.setAdapter(new TagAdapter<CommonWebBean.DataBean>(mCommonWebBean.getData()){

            @Override
            public View getView(FlowLayout parent, int position, CommonWebBean.DataBean dataBean) {
                TextView mTagTv = (TextView) LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.layout_flow_tag,parent,false);
                mTagTv.setText(dataBean.getName());
                mTagTv.setBackgroundColor(CommonUtil.getFlowTagBackgroudColor());
                return mTagTv;
            }
        });

        commonWebFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = ArticleDetailActivity.newIntent(CommonWebActivity.this,
                        mCommonWebBean.getData().get(position).getName(),mCommonWebBean.getData().get(position).getLink());
                startActivity(intent);
                return true;
            }
        });
    }
}
