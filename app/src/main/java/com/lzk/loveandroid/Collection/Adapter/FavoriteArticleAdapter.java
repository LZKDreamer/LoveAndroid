package com.lzk.loveandroid.Collection.Adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.Collection.Bean.FavoriteArticle;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Utils.ImageLoadUtil;

import java.util.List;

/**
 * @author LiaoZhongKai
 * @date 2019/9/25.
 */
public class FavoriteArticleAdapter extends BaseQuickAdapter<FavoriteArticle.DataBean.DatasBean, BaseViewHolder> {

    public FavoriteArticleAdapter(int layoutResId, @Nullable List<FavoriteArticle.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FavoriteArticle.DataBean.DatasBean item) {
        if (TextUtils.isEmpty(item.getEnvelopePic())){
            helper.getView(R.id.favorite_item_iv).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.favorite_item_iv).setVisibility(View.VISIBLE);
            ImageLoadUtil.load(mContext,item.getEnvelopePic(),helper.getView(R.id.favorite_item_iv));
        }

        helper.setText(R.id.favorite_item_author_tv,item.getAuthor());
        helper.setText(R.id.favorite_item_tag_tv,item.getChapterName());
        helper.setText(R.id.favorite_item_title_tv, Html.fromHtml(item.getTitle()));

        if (TextUtils.isEmpty(item.getDesc())){
            helper.getView(R.id.favorite_item_desc_tv).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.favorite_item_desc_tv).setVisibility(View.VISIBLE);
            helper.setText(R.id.favorite_item_desc_tv,Html.fromHtml(item.getDesc()));
        }

        helper.setText(R.id.favorite_item_time_tv,item.getNiceDate());
        helper.getView(R.id.favorite_item_unCollect_iv).setSelected(true);

        helper.addOnClickListener(R.id.favorite_item_unCollect_iv);
    }
}
