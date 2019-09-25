package com.lzk.loveandroid.Search.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.DB.Search.SearchHistoryBean;
import com.lzk.loveandroid.R;

import java.util.List;

public class SearchHistoryAdapter extends BaseQuickAdapter<SearchHistoryBean, BaseViewHolder> {
    public SearchHistoryAdapter(int layoutResId, @Nullable List<SearchHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchHistoryBean item) {
        helper.setText(R.id.search_history_item_tv,item.getKeyWord());
        helper.addOnClickListener(R.id.search_history_item_tv);
    }
}
