package com.lzk.loveandroid.Knowledge.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.Knowledge.Bean.KnowledgeItem;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Utils.LogUtil;

import java.util.List;

/**
 * @author LiaoZhongKai
 * @date 2019/9/18.
 */
public class KnowledgeItemAdapter extends BaseQuickAdapter<KnowledgeItem.DataBean.DatasBean, BaseViewHolder> {

    public KnowledgeItemAdapter(int layoutResId, @Nullable List<KnowledgeItem.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, KnowledgeItem.DataBean.DatasBean item) {
           helper.setText(R.id.content_item_author_tv,item.getAuthor());
           helper.setText(R.id.content_item_time_tv,item.getNiceDate());
           helper.setText(R.id.content_item_title_tv,item.getTitle());
           helper.setText(R.id.content_item_chapter_tv,item.getSuperChapterName()+"/"+item.getChapterName());
           helper.getView(R.id.content_item_collect_iv).setSelected(item.isCollect());
           helper.addOnClickListener(R.id.content_item_collect_iv);
    }
}
