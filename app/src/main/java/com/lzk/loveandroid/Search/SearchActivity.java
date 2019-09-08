package com.lzk.loveandroid.Search;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.App.MyApplication;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.DB.Search.SearchHistoryBean;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.LogUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.search_back_iv)
    ImageView searchBackIv;
    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.search_title_bar_ll)
    LinearLayout searchTitleBarLl;
    @BindView(R.id.search_hot_flow_layout)
    TagFlowLayout searchHotFlowLayout;
    @BindView(R.id.search_clear_tv)
    TextView searchClearTv;
    @BindView(R.id.search_history_rv)
    RecyclerView searchHistoryRv;
    @BindView(R.id.search_empty_hint_tv)
    TextView mSearchEmptyHintTv;

    private SearchHistoryAdapter mHistoryAdapter;

    private SearchHotkey mSearchHotkey;

    private List<SearchHistoryBean> mHistoryBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this,R.color.colorPrimaryToolbar));
        loadHotKey();
        loadHistoryData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @OnClick({R.id.search_back_iv, R.id.search_btn, R.id.search_clear_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_back_iv:
                finish();
                break;
            case R.id.search_btn:
                String key = searchEt.getText().toString().trim();
                if (!TextUtils.isEmpty(key)){
                    saveSearchHistory(key);
                    startSearchResultActivity(key);
                }
                break;
            case R.id.search_clear_tv:
                if (mHistoryBeanList != null && mHistoryBeanList.size() != 0){
                    LitePal.deleteAll(SearchHistoryBean.class);
                    searchHistoryRv.setVisibility(View.GONE);
                    mSearchEmptyHintTv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 查询搜索历史
     */
    private void loadHistoryData(){
        mHistoryBeanList = LitePal.findAll(SearchHistoryBean.class);
        if (mHistoryBeanList == null || mHistoryBeanList.size() == 0){
            searchHistoryRv.setVisibility(View.GONE);
            mSearchEmptyHintTv.setVisibility(View.VISIBLE);
        }else {
            if (mHistoryAdapter == null){
                mHistoryAdapter = new SearchHistoryAdapter(R.layout.layout_search_history_item,mHistoryBeanList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                searchHistoryRv.setLayoutManager(layoutManager);
                searchHistoryRv.setAdapter(mHistoryAdapter);
                mHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        startSearchResultActivity(mHistoryBeanList.get(position).getKeyWord());
                    }
                });
            }
        }
    }

    /**
     * 启动搜索结果页面
     * @param keyword
     */
    private void startSearchResultActivity(String keyword){
        finish();
    }

    /**
     * 查询热门搜索
     */
    private void loadHotKey(){
        RequestCenter.requestSearchHotKey(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                mSearchHotkey = (SearchHotkey) object;
                if (mSearchHotkey != null){
                    setHotkeyFlowLayout();
                }
            }

            @Override
            public void onFailure(int errCode, String errMsg) {

            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 显示热门搜索
     */
    private void setHotkeyFlowLayout(){
        searchHotFlowLayout.setAdapter(new TagAdapter<SearchHotkey.DataBean>(mSearchHotkey.getData()) {
            @Override
            public View getView(FlowLayout parent, int position, SearchHotkey.DataBean o) {
                TextView tag = (TextView) LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.layout_flow_tag,parent,false);
                tag.setText(o.getName());
                return tag;
            }
        });
        searchHotFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return true;
            }
        });
    }

    /**
     * 保存搜索历史
     * @param key
     */
    private void saveSearchHistory(String key){
        SearchHistoryBean bean = new SearchHistoryBean();
        bean.setKeyWord(key);
        bean.save();
    }
}
