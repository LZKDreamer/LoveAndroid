package com.lzk.loveandroid.Knowledge.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.Knowledge.Adapter.KnowledgePagerAdapter;
import com.lzk.loveandroid.Knowledge.Bean.KnowledgeBean;
import com.lzk.loveandroid.Knowledge.Fragment.KnowledgeListFragment;
import com.lzk.loveandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KnowledgeListActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView mCommonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar mCommonToolbar;
    @BindView(R.id.knowledge_list_tab_layout)
    TabLayout mKnowledgeListTabLayout;
    @BindView(R.id.knowledge_list_view_pager)
    ViewPager mKnowledgeListViewPager;
    @BindView(R.id.knowledge_list_float_btn)
    FloatingActionButton mFloatingActionButton;

    private KnowledgeBean.DataBean mDataBean;
    private List<String> mTitleList = new ArrayList<>();
    private List<KnowledgeListFragment> mFragmentList = new ArrayList<>();

    private KnowledgePagerAdapter mPagerAdapter;

    private static final String INTENT_DATA_BEAN = "intent_data_bean";

    public static Intent newIntent(Context context, KnowledgeBean.DataBean dataBean) {
        Intent intent = new Intent(context, KnowledgeListActivity.class);
        intent.putExtra(INTENT_DATA_BEAN, dataBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.toolbar));
        ButterKnife.bind(this);
        //Toolbar
        setSupportActionBar(mCommonToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back_white);
        }
        initTitle();
        initFragmentsAndTabs();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_knowledge_list;
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

    /**
     * 设置标题
     */
    private void initTitle() {
        if (getIntent() != null) {
            mDataBean = (KnowledgeBean.DataBean) getIntent().getSerializableExtra(INTENT_DATA_BEAN);
        }
        if (mDataBean != null) {
            mCommonToolbarTitleTv.setText(mDataBean.getName());
        }
    }

    /**
     * 初始化Fragment和标题
     */
    private void initFragmentsAndTabs() {
        if (mDataBean != null) {
            for (KnowledgeBean.DataBean.ChildrenBean bean : mDataBean.getChildren()) {
                mFragmentList.add(KnowledgeListFragment.newInstance(bean.getId()));
                mTitleList.add(bean.getName());
            }
        }

        mPagerAdapter = new KnowledgePagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mKnowledgeListViewPager.setCurrentItem(0);
        mKnowledgeListViewPager.setAdapter(mPagerAdapter);
        mKnowledgeListTabLayout.setupWithViewPager(mKnowledgeListViewPager);
    }

    @OnClick(R.id.knowledge_list_float_btn)
    public void onViewClicked() {
        backToTop();
    }

    /**
     * 返回顶部
     */
    private void backToTop(){
        if (mKnowledgeListTabLayout != null){
            int position = mKnowledgeListTabLayout.getSelectedTabPosition();
            if (mFragmentList.size() >= position ){
                mFragmentList.get(position).backToTop();
            }
        }
    }
}
