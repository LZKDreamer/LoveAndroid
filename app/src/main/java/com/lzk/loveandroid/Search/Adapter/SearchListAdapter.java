package com.lzk.loveandroid.Search.Adapter;

import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Search.Bean.SearchResult;
import com.lzk.loveandroid.Utils.LogUtil;

import java.util.List;

public class SearchListAdapter extends BaseQuickAdapter<SearchResult.DataBean.DatasBean, BaseViewHolder> {

    public SearchListAdapter(int layoutResId, @Nullable List<SearchResult.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchResult.DataBean.DatasBean item) {
        //作者
        helper.setText(R.id.article_item_author_tv,item.getAuthor());
        //时间
        helper.setText(R.id.article_item_time_tv,item.getNiceDate());
        //标题
        helper.setText(R.id.article_item_title_tv, Html.fromHtml(item.getTitle()));
        //类别
        helper.setText(R.id.article_item_chapter_tv,item.getChapterName()+"/"+item.getSuperChapterName());
        //收藏
        helper.getView(R.id.article_item_collect_iv).setSelected(item.isCollect());
        helper.addOnClickListener(R.id.article_item_collect_iv);
        LogUtil.d(item.getTitle());
    }
}
