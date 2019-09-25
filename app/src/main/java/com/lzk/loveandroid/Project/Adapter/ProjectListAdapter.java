package com.lzk.loveandroid.Project.Adapter;

import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.Project.Bean.ProjectList;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Utils.ImageLoadUtil;

import java.util.List;

public class ProjectListAdapter extends BaseQuickAdapter<ProjectList.DataBean.DatasBean, BaseViewHolder> {

    public ProjectListAdapter(int layoutResId, @Nullable List<ProjectList.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProjectList.DataBean.DatasBean item) {
        ImageLoadUtil.load(mContext,item.getEnvelopePic(),helper.getView(R.id.project_list_item_thumb_iv));
        helper.setText(R.id.project_list_item_title_tv, Html.fromHtml(item.getTitle()));
        helper.setText(R.id.project_list_item_desc_tv,Html.fromHtml(item.getDesc()));
        helper.setText(R.id.project_list_item_time_tv,item.getNiceDate());
        helper.setText(R.id.project_list_item_author_tv,item.getAuthor());
        helper.getView(R.id.project_list_item_collect_iv).setSelected(item.isCollect());
        helper.addOnClickListener(R.id.project_list_item_collect_iv);
    }
}
