package com.lzk.loveandroid.wx.Adapter;

import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.wx.Bean.WXArticle;

import java.util.List;

public class WXListAdapter extends BaseQuickAdapter<WXArticle.DataBean.DatasBean,BaseViewHolder> {

    public WXListAdapter(int layoutResId, @Nullable List<WXArticle.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WXArticle.DataBean.DatasBean item) {
        helper.setText(R.id.content_item_author_tv,item.getAuthor());
        helper.setText(R.id.content_item_time_tv,item.getNiceDate());
        helper.setText(R.id.content_item_title_tv, Html.fromHtml(item.getTitle()));
        helper.setText(R.id.content_item_chapter_tv,item.getSuperChapterName()+"/"+item.getChapterName());
        helper.getView(R.id.content_item_collect_iv).setSelected(item.isCollect());
        helper.addOnClickListener(R.id.content_item_collect_iv);
    }
}
