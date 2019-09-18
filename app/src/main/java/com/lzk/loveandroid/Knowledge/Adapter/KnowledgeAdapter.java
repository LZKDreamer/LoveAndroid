package com.lzk.loveandroid.Knowledge.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.Knowledge.Bean.KnowledgeBean;
import com.lzk.loveandroid.R;

import java.util.List;

/**
 * @author LiaoZhongKai
 * @date 2019/9/18.
 */
public class KnowledgeAdapter extends BaseQuickAdapter<KnowledgeBean.DataBean, BaseViewHolder> {
    public KnowledgeAdapter(int layoutResId, @Nullable List<KnowledgeBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, KnowledgeBean.DataBean item) {
        helper.setText(R.id.knowledge_item_title_tv,item.getName());
        StringBuilder tagBuilder = new StringBuilder();
        for (KnowledgeBean.DataBean.ChildrenBean bean : item.getChildren()){
            tagBuilder.append(bean.getName());
            tagBuilder.append("  ");
        }
        helper.setText(R.id.knowledge_item_tag_tv,tagBuilder);
    }
}
